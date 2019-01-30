package org.jenkinsci.plugins.gatlingcheck.data.metric;

/**
 * @author xiaoyao
 */
public class GatlingPercentageMetric {

    private String name;

    private double count;

    private double percentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
