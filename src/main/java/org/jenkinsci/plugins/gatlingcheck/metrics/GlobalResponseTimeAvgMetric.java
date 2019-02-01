package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;
import static org.jenkinsci.plugins.gatlingcheck.util.FormValidationUtils.isPositiveNumber;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getResponseTimeAvg;

/**
 * @author xiaoyao
 */
public class GlobalResponseTimeAvgMetric extends AbstractMetric {

    private final String responseTime;

    @DataBoundConstructor
    public GlobalResponseTimeAvgMetric(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.GLOBAL_RESPONSE_TIME_AVG;
    }

    @Override
    public boolean check(
            @Nullable TaskListener taskListener, @Nonnull GatlingReport gatlingReport
    ) {
        double expected = Double.valueOf(responseTime);
        double actual = getResponseTimeAvg(gatlingReport);
        if (actual > expected) {
            logError(taskListener, format(
                    "global avg response time metric unqualified, expected = %f, actual = %f",
                    expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "global avg response time metric accepted, expected = %f, actual = %f",
                    expected, actual
            ));
            return true;
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Global Average Response Time Pre-warning";
        }

        public FormValidation doCheckResponseTime(@QueryParameter String responseTime) {
            return isPositiveNumber(responseTime);
        }
    }

    public String getResponseTime() {
        return responseTime;
    }
}
