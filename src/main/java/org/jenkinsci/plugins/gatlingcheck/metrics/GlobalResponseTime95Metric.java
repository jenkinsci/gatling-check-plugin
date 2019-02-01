package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

import static java.lang.String.format;

public final class GlobalResponseTime95Metric extends AbstractMetric {

    private final String responseTime;

    @DataBoundConstructor
    public GlobalResponseTime95Metric(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.GLOBAL_RESPONSE_TIME_95;
    }

    @Override
    public boolean check(
            @Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport
    ) {
        double expected = Double.valueOf(responseTime);
        double actual = GatlingReportUtils.getResponseTime95(gatlingReport);
        if (actual > expected) {
            logError(taskListener, format(
                    "global .95 response time metric unqualified, expected = %f, actual = %f",
                    expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "global .95 response time metric accepted, expected = %f, actual = %f",
                    expected, actual
            ));
            return true;
        }
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
