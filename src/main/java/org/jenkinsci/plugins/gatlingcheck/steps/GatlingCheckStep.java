package org.jenkinsci.plugins.gatlingcheck.steps;

import com.google.common.collect.ImmutableSet;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.gatlingcheck.metrics.AbstractMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.GlobalOKRateMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.GlobalQpsMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.GlobalResponseTime95Metric;
import org.jenkinsci.plugins.gatlingcheck.metrics.GlobalResponseTime99Metric;
import org.jenkinsci.plugins.gatlingcheck.metrics.GlobalResponseTimeAvgMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.RequestOKRateMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.RequestQpsMetric;
import org.jenkinsci.plugins.gatlingcheck.metrics.RequestResponseTime95Metric;
import org.jenkinsci.plugins.gatlingcheck.metrics.RequestResponseTime99Metric;
import org.jenkinsci.plugins.gatlingcheck.metrics.RequestResponseTimeAvgMetric;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.split;

/**
 * @author xiaoyao
 */
public class GatlingCheckStep extends Step {

    private final List<String> metrics;

    @DataBoundConstructor
    public GatlingCheckStep(List<String> metrics) {
        this.metrics = metrics;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new GatlingCheckStepExecution(context, metrics.stream()
                .map(this::parseMetric)
                .filter(Objects::nonNull)
                .collect(toList()));
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public Set<Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }

        @Override
        public String getFunctionName() {
            return "gatlingCheck";
        }
    }

    public List<String> getMetrics() {
        return metrics;
    }

    private AbstractMetric parseMetric(String metric) {
        if (isBlank(metric)) {
            return null;
        }
        replace(metric, " ", "");

        String[] pair1 = split(metric, "=");
        if (pair1.length != 2) {
            return null;
        }
        String key = pair1[0];
        String value = pair1[1];

        String[] pair2 = split(key, ".");
        if (pair2.length != 2) {
            return null;
        }
        String scope = pair2[0];
        String type = pair2[1];

        if ("global".equalsIgnoreCase(scope)) {
            if ("qps".equalsIgnoreCase(type)) {
                return new GlobalQpsMetric(value);

            } else if ("okRate".equalsIgnoreCase(type)) {
                return new GlobalOKRateMetric(value);

            } else if ("responseTime99".equalsIgnoreCase(type)) {
                return new GlobalResponseTime99Metric(value);

            } else if ("responseTime95".equalsIgnoreCase(type)) {
                return new GlobalResponseTime95Metric(value);

            } else if ("responseTimeAvg".equalsIgnoreCase(type)) {
                return new GlobalResponseTimeAvgMetric(value);

            } else {
                return null;
            }

        } else {
            if ("qps".equalsIgnoreCase(type)) {
                return new RequestQpsMetric(scope, value);

            } else if ("okRate".equalsIgnoreCase(type)) {
                return new RequestOKRateMetric(scope, value);

            } else if ("responseTime99".equalsIgnoreCase(type)) {
                return new RequestResponseTime99Metric(scope, value);

            } else if ("responseTime95".equalsIgnoreCase(type)) {
                return new RequestResponseTime95Metric(scope, value);

            } else if ("responseTimeAvg".equalsIgnoreCase(type)) {
                return new RequestResponseTimeAvgMetric(scope, value);

            } else {
                return null;
            }
        }
    }
}
