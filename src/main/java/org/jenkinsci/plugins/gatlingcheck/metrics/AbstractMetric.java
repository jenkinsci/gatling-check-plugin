package org.jenkinsci.plugins.gatlingcheck.metrics;

import hudson.model.AbstractDescribableImpl;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.constant.MetricType;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;

import javax.annotation.Nonnull;

import static java.lang.String.format;

public abstract class AbstractMetric extends AbstractDescribableImpl<AbstractMetric> {

    public abstract MetricType getType();

    public abstract boolean check(
            @Nonnull TaskListener taskListener, @Nonnull GatlingReport gatlingReport
    );

    protected void log(TaskListener taskListener, String s) {
        taskListener.getLogger().println(format("[Gatling Check Plugin] %s", s));
    }

    protected void logError(TaskListener taskListener, String s) {
        taskListener.error("[Gatling Check Plugin] %s", s);
    }
}
