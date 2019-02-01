package org.jenkinsci.plugins.gatlingcheck.data;

import java.util.Map;

/**
 * @author xiaoyao
 */
public class GatlingReport {

    private String type;

    private String name;

    private String path;

    private String pathFormatted;

    private GatlingReportStats stats;

    private Map<String, GatlingReport> contents;

    public Map<String, GatlingReport> getContents() {
        return contents;
    }

    public void setContents(Map<String, GatlingReport> contents) {
        this.contents = contents;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathFormatted() {
        return pathFormatted;
    }

    public void setPathFormatted(String pathFormatted) {
        this.pathFormatted = pathFormatted;
    }

    public GatlingReportStats getStats() {
        return stats;
    }

    public void setStats(GatlingReportStats stats) {
        this.stats = stats;
    }
}
