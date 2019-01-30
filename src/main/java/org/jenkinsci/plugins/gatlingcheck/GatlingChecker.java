package org.jenkinsci.plugins.gatlingcheck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
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

    private final boolean enabled;

    @DataBoundConstructor
    public GatlingChecker(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener

    ) throws InterruptedException, IOException {

        if (!enabled) {
            logError(taskListener, "plugin not enabled.");
            return;
        }

        List<FilePath> statsFiles = getStatsFiles(run, filePath, taskListener);
        if (isEmpty(statsFiles)) {
            logError(taskListener, "stats-file not found.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        for (FilePath statsFile : statsFiles) {
            log(taskListener, "stats-file located: " + statsFile.getRemote());
            GatlingReport gatlingReport  = objectMapper.readValue(
                    new File(statsFile.getRemote()),
                    new TypeReference<GatlingReport>() {

                    }
            );
            log(taskListener, "QPS: " + gatlingReport.getStats().getNumberOfRequests().getOk());
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
}