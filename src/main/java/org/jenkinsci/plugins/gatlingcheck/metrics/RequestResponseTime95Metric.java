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
import java.util.Optional;

import static java.lang.String.format;
import static org.jenkinsci.plugins.gatlingcheck.util.FormValidationUtils.isPositiveNumber;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getRequestReportByName;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getResponseTime95;

/**
 * @author xiaoyao
 */
public class RequestResponseTime95Metric extends AbstractMetric {

    private final String requestName;

    private final String responseTime;

    @DataBoundConstructor
    public RequestResponseTime95Metric(String requestName, String responseTime) {
        this.requestName = requestName;
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.REQUEST_RESPONSE_TIME_95;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        Optional<GatlingReport> requestReport = getRequestReportByName(gatlingReport, requestName);
        if (!requestReport.isPresent()) {
            logError(taskListener, "request-report not found: " + requestName);
            return false;
        }

        double expected = Double.valueOf(responseTime);
        double actual = getResponseTime95(requestReport.get());
        if (actual > expected) {
            logError(taskListener, format(
                    "request %s .95 response time metric unqualified, expected = %f, actual = %f",
                    requestName, expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "request %s .95 response time metric accepted, expected = %f, actual = %f",
                    requestName, expected, actual
            ));
            return true;
        }
    }


    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Request .95 Response Time Pre-warning";
        }

        public FormValidation doCheckResponseTime(@QueryParameter String responseTime) {
            return isPositiveNumber(responseTime);
        }
    }

    public String getRequestName() {
        return requestName;
    }

    public String getResponseTime() {
        return responseTime;
    }
}
