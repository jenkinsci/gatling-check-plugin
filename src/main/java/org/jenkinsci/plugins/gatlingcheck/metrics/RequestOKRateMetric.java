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
public class RequestOKRateMetric extends AbstractMetric {

    private final String requestName;

    private final String rate;

    @DataBoundConstructor
    public RequestOKRateMetric(String requestName, String rate) {
        this.requestName = requestName;
        this.rate = rate;
    }

    @Override
    public MetricType getType() {
        return MetricType.REQUEST_OK_RATE;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        Optional<GatlingReport> requestReport = getRequestReportByName(gatlingReport, requestName);
        if (!requestReport.isPresent()) {
            logError(taskListener, "request-report not found: " + requestName);
            return false;
        }

        double expected = Double.valueOf(rate);
        double actual = GatlingReportUtils.getOKRate(requestReport.get());
        if (actual < expected) {
            logError(taskListener, format(
                    "request %s ok rate metric unqualified, expected = %f, actual = %f",
                    requestName, expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "request %s ok rate metric accepted, expected = %f, actual = %f",
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
            return "Request OK Rate Pre-warning";
        }
    }

    public String getRate() {
        return rate;
    }

    public String getRequestName() {
        return requestName;
    }
}
