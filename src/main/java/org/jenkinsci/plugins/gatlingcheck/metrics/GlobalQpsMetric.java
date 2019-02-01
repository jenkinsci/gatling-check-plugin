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

public final class GlobalQpsMetric extends AbstractMetric {

    private final String qps;

    @Override
    public MetricType getType() {
        return MetricType.GLOBAL_QPS;
    }

    @Override
    public boolean check(@Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport) {
        double expected = Double.valueOf(qps);
        double actual = GatlingReportUtils.getQps(gatlingReport);
        if (actual < expected) {
            logError(taskListener, format(
                    "global qps metric unqualified, expected = %f, actual = %f",
                    expected, actual
            ));
            return false;

        } else {
            log(taskListener, format(
                    "global qps metric accepted, expected = %f, actual = %f",
                    expected, actual
            ));
            return true;
        }
    }

    @DataBoundConstructor
    public GlobalQpsMetric(String qps) {
        this.qps = qps;
    }

    @Override
    public String toString() {
        return "global qps > " + qps;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {
        @Nonnull
        @Override
        public String getDisplayName() {
            return "Global QPS Pre-warning";
        }
    }

    public String getQps() {
        return qps;
    }

    public double getQpsAsDouble() {
        return Double.valueOf(qps);
    }
}
