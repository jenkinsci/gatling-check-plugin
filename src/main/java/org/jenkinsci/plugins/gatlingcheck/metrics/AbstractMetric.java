package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.model.AbstractDescribableImpl;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;

public abstract class AbstractMetric extends AbstractDescribableImpl<AbstractMetric> {

    public abstract MetricType getType();

    public abstract boolean check(GatlingReport gatlingReport);

    public abstract String toString();
}
