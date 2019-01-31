package org.jenkinsci.plugins.gatlingcheck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractDescribableImpl;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
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

        for (AbstractMetric metric : metrics) {
            switch (metric.getType()) {
                case QPS:
                    for (FilePath statsFile : statsFiles) {
                        double qps = getQps(statsFile);
                        if (qps < ((QpsMetric) metric).getQpsAsDouble()) {
                            logError(taskListener, format(
                                    "qps metric unqualified, %f < %s", qps, ((QpsMetric) metric).getQps()
                            ));
                            run.setResult(Result.FAILURE);

                        } else {
                            log(taskListener, "qps metric accepted: " + statsFile.getRemote());
                        }
                    }
                    break;

                case RESPONSE_TIME_99:
                    for (FilePath statsFile : statsFiles) {
                        double responseTime99 = getResponseTime99(statsFile);
                        if (responseTime99 > ((ResponseTime99Metric) metric).getResponseTimeAsDouble()) {
                            logError(taskListener, format(
                                    ".99 response time unqualified, %f > %s",
                                    responseTime99,
                                    ((ResponseTime99Metric) metric).getResponseTime()
                            ));
                            run.setResult(Result.FAILURE);

                        } else {
                            log(taskListener, ".99 response time metric accepted: " + statsFile.getRemote());
                        }
                    }
                    break;

                case RESPONSE_TIME_95:
                    for (FilePath statsFile : statsFiles) {
                        double responseTime95 = getResponseTime95(statsFile);
                        if (responseTime95 > ((ResponseTime95Metric) metric).getResponseTimeAsDouble()) {
                            logError(taskListener, format(
                                    ".95 response time unqualified, %f > %s",
                                    responseTime95,
                                    ((ResponseTime95Metric) metric).getResponseTime()
                            ));
                            run.setResult(Result.FAILURE);

                        } else {
                            log(taskListener, ".99 response time metric accepted: " + statsFile.getRemote());
                        }
                    }
                    break;
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

    private double getResponseTime95(FilePath statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GatlingReport gatlingReport = objectMapper.readValue(
                new File(statsFile.getRemote()),
                new TypeReference<GatlingReport>() {

                }
        );
        return gatlingReport.getStats().getPercentiles3().getOk();
    }

    private double getResponseTime99(FilePath statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GatlingReport gatlingReport = objectMapper.readValue(
                new File(statsFile.getRemote()),
                new TypeReference<GatlingReport>() {

                }
        );
        return gatlingReport.getStats().getPercentiles4().getOk();
    }

    private double getQps(FilePath statsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GatlingReport gatlingReport = objectMapper.readValue(
                new File(statsFile.getRemote()),
                new TypeReference<GatlingReport>() {

                }
        );
        return gatlingReport.getStats().getNumberOfRequests().getOk();
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

    public static abstract class AbstractMetric extends AbstractDescribableImpl<AbstractMetric> {
        public abstract MetricType getType();
    }

    public static final class ResponseTime99Metric extends AbstractMetric {

        private final String responseTime;

        @DataBoundConstructor
        public ResponseTime99Metric(String responseTime) {
            this.responseTime = responseTime;
        }

        @Override
        public MetricType getType() {
            return MetricType.RESPONSE_TIME_99;
        }

        @Extension
        public static class DescriptorImpl extends Descriptor<AbstractMetric> {

            @Nonnull
            @Override
            public String getDisplayName() {
                return ".99 响应时间预警";
            }
        }

        public String getResponseTime() {
            return responseTime;
        }

        public double getResponseTimeAsDouble() {
            return Double.valueOf(responseTime);
        }
    }

    public static final class ResponseTime95Metric extends AbstractMetric {

        private final String responseTime;

        @DataBoundConstructor
        public ResponseTime95Metric(String responseTime) {
            this.responseTime = responseTime;
        }

        @Override
        public MetricType getType() {
            return MetricType.RESPONSE_TIME_95;
        }

        @Extension
        public static class DescriptorImpl extends Descriptor<AbstractMetric> {

            @Nonnull
            @Override
            public String getDisplayName() {
                return ".95 响应时间预警";
            }
        }

        public String getResponseTime() {
            return responseTime;
        }

        public double getResponseTimeAsDouble() {
            return Double.valueOf(responseTime);
        }
    }

    public static final class QpsMetric extends AbstractMetric {

        private final String qps;

        @Override
        public MetricType getType() {
            return MetricType.QPS;
        }

        @DataBoundConstructor
        public QpsMetric(String qps) {
            this.qps = qps;
        }

        @Extension
        public static class DescriptorImpl extends Descriptor<AbstractMetric> {
            @Nonnull
            @Override
            public String getDisplayName() {
                return "QPS 预警";
            }
        }

        public String getQps() {
            return qps;
        }

        public double getQpsAsDouble() {
            return Double.valueOf(qps);
        }
    }

    public List<AbstractMetric> getMetrics() {
        return metrics;
    }
}