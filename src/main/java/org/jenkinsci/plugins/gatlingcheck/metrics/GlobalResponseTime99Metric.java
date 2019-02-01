package org.jenkinsci.plugins.gatlingcheck.metrics;


import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

public final class GlobalResponseTime99Metric extends AbstractMetric {

    private final String responseTime;

    @DataBoundConstructor
    public GlobalResponseTime99Metric(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.GLOBAL_RESPONSE_TIME_99;
    }

    @Override
    public boolean check(
            @Nullable TaskListener taskListener, @Nonnull GatlingReport gatlingReport
    ) {
        double expected = Double.valueOf(responseTime);
        double actual = GatlingReportUtils.getResponseTime99(gatlingReport);
        if (actual > expected) {
            logError(taskListener, format(
                    "global .99 response time metric unqualified, expected = %f, actual = %f",
                    expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "global .99 response time metric accepted, expected = %f, actual = %f",
                    expected, actual
            ));
            return true;
        }
    }

    @Override
    public String toString() {
        return "global .99 response time < " + responseTime + " (ms)";
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Global .99 Response Time Pre-warning";
        }
    }

    public String getResponseTime() {
        return responseTime;
    }

    public double getResponseTimeAsDouble() {
        return Double.valueOf(responseTime);
    }
}
