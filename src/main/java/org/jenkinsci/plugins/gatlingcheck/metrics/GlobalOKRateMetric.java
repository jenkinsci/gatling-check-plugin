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

import static java.lang.String.format;
import static org.jenkinsci.plugins.gatlingcheck.util.FormValidationUtils.isValidRate;

/**
 * @author xiaoyao
 */
public class GlobalOKRateMetric extends AbstractMetric {

    private final String rate;

    @DataBoundConstructor
    public GlobalOKRateMetric(String rate) {
        this.rate = rate;
    }

    @Override
    public MetricType getType() {
        return MetricType.GLOBAL_OK_RATE;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        double expected = Double.valueOf(rate);
        double actual = GatlingReportUtils.getOKRate(gatlingReport);
        if (actual < expected) {
            logError(taskListener, format(
                    "global ok rate metric unqualified, expected = %f, actual = %f",
                    expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "global ok rate metric accepted, expected = %f, actual = %f",
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
            return "Global OK Rate Pre-warning";
        }

        public FormValidation doCheckRate(@QueryParameter String rate) {
            return isValidRate(rate);
        }
    }

    public String getRate() {
        return rate;
    }
}
