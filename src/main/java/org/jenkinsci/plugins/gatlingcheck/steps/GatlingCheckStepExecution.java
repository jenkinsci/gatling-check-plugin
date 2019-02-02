package org.jenkinsci.plugins.gatlingcheck.steps;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.GatlingChecker;
import org.jenkinsci.plugins.gatlingcheck.metrics.AbstractMetric;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.util.List;

/**
 * @author xiaoyao
 */
public class GatlingCheckStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

    private static final long serialVersionUID = 1L;

    private final List<AbstractMetric> metrics;

    public GatlingCheckStepExecution(StepContext context, List<AbstractMetric> metrics) {
        super(context);
        this.metrics = metrics;
    }

    @Override
    protected Void run() throws Exception {
        TaskListener listener = getContext().get(TaskListener.class);
        FilePath ws = getContext().get(FilePath.class);
        Run build = getContext().get(Run.class);
        Launcher launcher = getContext().get(Launcher.class);

        listener.getLogger().println("----------------------------------------------------------");

        GatlingChecker gatlingChecker = new GatlingChecker(metrics);
        gatlingChecker.perform(build, ws, launcher, listener);

        return null;
    }

    public List<AbstractMetric> getMetrics() {
        return metrics;
    }
}
