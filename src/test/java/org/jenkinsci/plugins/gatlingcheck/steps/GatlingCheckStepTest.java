package org.jenkinsci.plugins.gatlingcheck.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.BuildWatcher;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author xiaoyao
 */
public class GatlingCheckStepTest {

    @Rule
    public JenkinsRule r = new JenkinsRule();

    @ClassRule
    public static BuildWatcher buildWatcher = new BuildWatcher();

    @Test
    public void testPipeline() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        GatlingReport gatlingGlobalReport = objectMapper.readValue(
                new File("src/test/resources/stats.json"),
                new TypeReference<GatlingReport>() {

                }
        );
        String stats = objectMapper.writeValueAsString(gatlingGlobalReport);

        WorkflowJob p = r.jenkins.createProject(WorkflowJob.class, "p");
        p.setDefinition(new CpsFlowDefinition(join(
                "node {\n",
                "    sleep 1\n",
                "    writeFile file: 'results/p-1234/js/stats.json', text: '" + stats + "'\n",
                "    gatlingCheck(metrics: ['global.qps=100', 'global.okRate = 90'])\n",
                "}\n"
        )));

        WorkflowRun b = r.assertBuildStatusSuccess(p.scheduleBuild2(0));
    }
}