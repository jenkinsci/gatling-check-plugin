package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getResponseTime95;

public final class GlobalResponseTime95Metric extends AbstractMetric {

    private final String responseTime;

    @DataBoundConstructor
    public GlobalResponseTime95Metric(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.RESPONSE_TIME_95;
    }

    @Override
    public boolean check(GatlingReport gatlingReport) {
        return getResponseTime95(gatlingReport) < Double.valueOf(responseTime);
    }

    @Override
    public String toString() {
        return "global .95 response time < " + responseTime + " (ms)";
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Global .95 Response Time Pre-warning";
        }
    }

    public String getResponseTime() {
        return responseTime;
    }

    public double getResponseTimeAsDouble() {
        return Double.valueOf(responseTime);
    }
}
