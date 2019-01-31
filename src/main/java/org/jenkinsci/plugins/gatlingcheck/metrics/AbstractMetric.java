package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.model.AbstractDescribableImpl;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;

public abstract class AbstractMetric extends AbstractDescribableImpl<AbstractMetric> {

    public abstract MetricType getType();
}
