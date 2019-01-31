package org.jenkinsci.plugins.gatlingcheck.steps;

import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class GatlingCheckStep extends AbstractStepImpl {

    @DataBoundConstructor
    public GatlingCheckStep() {
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(GatlingCheckStepExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "gatlingCheck";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Check Gatling reports";
        }
    }
}
