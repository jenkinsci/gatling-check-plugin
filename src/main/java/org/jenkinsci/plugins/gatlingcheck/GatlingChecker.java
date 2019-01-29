package org.jenkinsci.plugins.gatlingcheck;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GatlingChecker extends Recorder implements SimpleBuildStep {

    private final boolean enabled;

    @DataBoundConstructor
    public GatlingChecker(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void perform(
            @Nonnull Run<?, ?> run,
            @Nonnull FilePath filePath,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener taskListener

    ) throws InterruptedException, IOException {

        if (!enabled) {
            taskListener.getLogger().println("Gatling Check Plugin not enabled.");

        } else {
            taskListener.getLogger().println("Gatling Check Plugin not implemented.");
            run.setResult(Result.FAILURE);
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }
}