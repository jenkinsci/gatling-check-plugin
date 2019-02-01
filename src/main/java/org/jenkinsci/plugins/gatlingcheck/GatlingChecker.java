package org.jenkinsci.plugins.gatlingcheck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.gatlingcheck.metrics.AbstractMetric;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class GatlingChecker extends Recorder implements SimpleBuildStep {

    private final List<AbstractMetric> metrics;

    @DataBoundConstructor
    public GatlingChecker(List<AbstractMetric> metrics) {
        this.metrics = isEmpty(metrics) ? emptyList() : metrics;
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener

    ) throws InterruptedException, IOException {

        if (isEmpty(metrics)) {
            logError(taskListener, "no metric specified.");
            return;
        }

        List<FilePath> statsFiles = getStatsFiles(run, filePath, taskListener);
        if (isEmpty(statsFiles)) {
            logError(taskListener, "stats-file not found.");
            return;
        }

        for (FilePath statsFile : statsFiles) {
            GatlingReport gatlingGlobalReport = getGatlingReport(statsFile);
            log(taskListener, "Checking " + statsFile.getRemote());
            for (AbstractMetric metric : metrics) {
                if (metric.check(gatlingGlobalReport)) {
                    log(taskListener, "metric accepted: " + metric);

                } else {
                    logError(taskListener, "metric unqualified: " + metric);
                    run.setResult(Result.FAILURE);
                }
            }
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Gatling Checker";
        }
    }

    private GatlingReport getGatlingReport(FilePath statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                new File(statsFile.getRemote()),
                new TypeReference<GatlingReport>() {

                }
        );
    }

    private List<FilePath> getStatsFiles(
            Run<?, ?> run, FilePath filePath, TaskListener taskListener

    ) throws InterruptedException, IOException {

        FilePath[] files = filePath.list("**/stats.json");
        if (files.length == 0) {
            log(taskListener, "Gatling Check Plugin: ");
            return emptyList();
        }

        List<FilePath> statsFiles = new ArrayList<>();
        for (FilePath file : files) {
            if (file.lastModified() > run.getStartTimeInMillis()) {
                statsFiles.add(file);
            }
        }
        return statsFiles;
    }

    private void logError(TaskListener taskListener, String s) {
        taskListener.error("[Gatling Check Plugin]: %s", s);
    }

    private void log(TaskListener taskListener, String s) {
        taskListener.getLogger().println(format("[Gatling Check Plugin]: %s", s));
    }

    public List<AbstractMetric> getMetrics() {
        return metrics;
    }
}