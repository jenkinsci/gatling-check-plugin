package org.jenkinsci.plugins.gatlingcheck.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jenkinsci.plugins.gatlingcheck.util.GatlingReportUtils.getRequestNames;
import static org.junit.Assert.assertThat;

/**
 * @author xiaoyao
 */
public class GatlingReportUtilsTest {

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue(
                new File("src/test/resources/stats.json"),
                new TypeReference<GatlingReport>() {

                }
        );
    }

    @Test
    public void testGetRequestNames() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        GatlingReport gatlingGlobalReport = objectMapper.readValue(
                new File("src/test/resources/stats.json"),
                new TypeReference<GatlingReport>() {

                }
        );
        List<String> requestNames = getRequestNames(gatlingGlobalReport);
        assertThat(requestNames, containsInAnyOrder("get-homework-list-1", "get-homework-list-2"));
    }
}