package org.jenkinsci.plugins.gatlingcheck.steps;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.GatlingChecker;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import static java.util.Collections.emptyList;

public class GatlingCheckStepExecution
        extends AbstractSynchronousNonBlockingStepExecution<Void> {

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient FilePath ws;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    @Override
    protected Void run() throws Exception {
        listener.getLogger().println("Running Gatling check step.");

        GatlingChecker gatlingChecker = new GatlingChecker(emptyList());
        gatlingChecker.perform(build, ws, launcher, listener);

        return null;
    }
}
