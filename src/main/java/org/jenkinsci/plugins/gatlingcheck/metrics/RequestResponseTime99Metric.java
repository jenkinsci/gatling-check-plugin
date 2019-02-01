package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.Optional;

import static java.lang.String.format;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getRequestReportByName;

/**
 * @author xiaoyao
 */
public class RequestResponseTime99Metric extends AbstractMetric{
    
    private final String requestName;

    private final String responseTime;

    @DataBoundConstructor
    public RequestResponseTime99Metric(String requestName, String responseTime) {
        this.requestName = requestName;
        this.responseTime = responseTime;
    }

    @Override
    public MetricType getType() {
        return MetricType.REQUEST_RESPONSE_TIME_99;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        Optional<GatlingReport> requestReport = getRequestReportByName(gatlingReport, requestName);
        if (!requestReport.isPresent()) {
            logError(taskListener, "request-report not found: " + requestName);
            return false;
        }

        double expected = Double.valueOf(responseTime);
        double actual = GatlingReportUtils.getResponseTime99(requestReport.get());
        if (actual > expected) {
            logError(taskListener, format(
                    "request %s .99 response time metric unqualified, expected = %f, actual = %f",
                    requestName, expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "request %s .99 response time metric accepted, expected = %f, actual = %f",
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
            return "Request .99 Response Time Pre-warning";
        }
    }

    public String getRequestName() {
        return requestName;
    }

    public String getResponseTime() {
        return responseTime;
    }
}
