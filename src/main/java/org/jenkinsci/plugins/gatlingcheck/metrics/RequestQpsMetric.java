package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.util.Optional;

import static java.lang.String.format;
import static org.jenkinsci.plugins.gatlingcheck.util.FormValidationUtils.isPositiveNumber;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getRequestReportByName;

/**
 * @author xiaoyao
 */
public class RequestQpsMetric extends AbstractMetric {

    private final String requestName;

    private final String qps;

    @DataBoundConstructor
    public RequestQpsMetric(String requestName, String qps) {
        this.requestName = requestName;
        this.qps = qps;
    }

    @Override
    public MetricType getType() {
        return MetricType.REQUEST_QPS;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        Optional<GatlingReport> requestReport = getRequestReportByName(gatlingReport, requestName);
        if (!requestReport.isPresent()) {
            logError(taskListener, "request-report not found: " + requestName);
            return false;
        }

        double expected = Double.valueOf(qps);
        double actual = GatlingReportUtils.getQps(requestReport.get());
        if (actual < expected) {
            logError(taskListener, format(
                    "request %s qps metric unqualified, expected = %f, actual = %f",
                    requestName, expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "request %s qps metric accepted, expected = %f, actual = %f",
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
            return "Request QPS Pre-warning";
        }

        public FormValidation doCheckQps(@QueryParameter String qps) {
            return isPositiveNumber(qps);
        }
    }

    public String getRequestName() {
        return requestName;
    }

    public String getQps() {
        return qps;
    }
}
