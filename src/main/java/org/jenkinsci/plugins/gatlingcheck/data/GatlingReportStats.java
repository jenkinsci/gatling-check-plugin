package org.jenkinsci.plugins.gatlingcheck.data;

import org.jenkinsci.plugins.gatlingcheck.data.stats.GatlingCountStat;
import org.jenkinsci.plugins.gatlingcheck.data.stats.GatlingPercentageStat;

/**
 * @author xiaoyao
 */
public class GatlingReportStats {

    private String name;

    private GatlingCountStat numberOfRequests;

    private GatlingCountStat minResponseTime;

    private GatlingCountStat maxResponseTime;

    private GatlingCountStat meanResponseTime;

    private GatlingCountStat standardDeviation;

    private GatlingCountStat percentiles1;

    private GatlingCountStat percentiles2;

    private GatlingCountStat percentiles3;

    private GatlingCountStat percentiles4;

    private GatlingPercentageStat group1;

    private GatlingPercentageStat group2;

    private GatlingPercentageStat group3;

    private GatlingPercentageStat group4;

    private GatlingCountStat meanNumberOfRequestsPerSecond;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GatlingCountStat getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(GatlingCountStat numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public GatlingCountStat getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(GatlingCountStat minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public GatlingCountStat getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(GatlingCountStat maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public GatlingCountStat getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(GatlingCountStat meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public GatlingCountStat getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(GatlingCountStat standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public GatlingCountStat getPercentiles1() {
        return percentiles1;
    }

    public void setPercentiles1(GatlingCountStat percentiles1) {
        this.percentiles1 = percentiles1;
    }

    public GatlingCountStat getPercentiles2() {
        return percentiles2;
    }

    public void setPercentiles2(GatlingCountStat percentiles2) {
        this.percentiles2 = percentiles2;
    }

    public GatlingCountStat getPercentiles3() {
        return percentiles3;
    }

    public void setPercentiles3(GatlingCountStat percentiles3) {
        this.percentiles3 = percentiles3;
    }

    public GatlingCountStat getPercentiles4() {
        return percentiles4;
    }

    public void setPercentiles4(GatlingCountStat percentiles4) {
        this.percentiles4 = percentiles4;
    }

    public GatlingPercentageStat getGroup1() {
        return group1;
    }

    public void setGroup1(GatlingPercentageStat group1) {
        this.group1 = group1;
    }

    public GatlingPercentageStat getGroup2() {
        return group2;
    }

    public void setGroup2(GatlingPercentageStat group2) {
        this.group2 = group2;
    }

    public GatlingPercentageStat getGroup3() {
        return group3;
    }

    public void setGroup3(GatlingPercentageStat group3) {
        this.group3 = group3;
    }

    public GatlingPercentageStat getGroup4() {
        return group4;
    }

    public void setGroup4(GatlingPercentageStat group4) {
        this.group4 = group4;
    }

    public GatlingCountStat getMeanNumberOfRequestsPerSecond() {
        return meanNumberOfRequestsPerSecond;
    }

    public void setMeanNumberOfRequestsPerSecond(GatlingCountStat meanNumberOfRequestsPerSecond) {
        this.meanNumberOfRequestsPerSecond = meanNumberOfRequestsPerSecond;
    }
}
