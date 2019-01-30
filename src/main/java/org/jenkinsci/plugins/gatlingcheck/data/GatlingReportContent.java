package org.jenkinsci.plugins.gatlingcheck.data;

/**
 * @author xiaoyao
 */
public class GatlingReportContent {

    private String type;

    private String name;

    private String path;

    private String pathFormatted;

    private GatlingReportStats stats;

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
