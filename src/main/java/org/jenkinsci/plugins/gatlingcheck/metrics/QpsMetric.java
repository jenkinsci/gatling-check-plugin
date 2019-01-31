package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public final class QpsMetric extends AbstractMetric {

    private final String qps;

    @Override
    public MetricType getType() {
        return MetricType.QPS;
    }

    @DataBoundConstructor
    public QpsMetric(String qps) {
        this.qps = qps;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AbstractMetric> {
        @Nonnull
        @Override
        public String getDisplayName() {
            return "QPS 预警";
        }
    }

    public String getQps() {
        return qps;
    }

    public double getQpsAsDouble() {
        return Double.valueOf(qps);
    }
}
