package org.jenkinsci.plugins.gatlingcheck.data;

import org.jenkinsci.plugins.gatlingcheck.data.metric.GatlingCountMetric;
import org.jenkinsci.plugins.gatlingcheck.data.metric.GatlingPercentageMetric;

/**
 * @author xiaoyao
 */
public class GatlingReportStats {

    private String name;

    private GatlingCountMetric numberOfRequests;

    private GatlingCountMetric minResponseTime;

    private GatlingCountMetric maxResponseTime;

    private GatlingCountMetric meanResponseTime;

    private GatlingCountMetric standardDeviation;

    private GatlingCountMetric percentiles1;

    private GatlingCountMetric percentiles2;

    private GatlingCountMetric percentiles3;

    private GatlingCountMetric percentiles4;

    private GatlingPercentageMetric group1;

    private GatlingPercentageMetric group2;

    private GatlingPercentageMetric group3;

    private GatlingPercentageMetric group4;

    private GatlingCountMetric meanNumberOfRequestsPerSecond;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GatlingCountMetric getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(
            GatlingCountMetric numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public GatlingCountMetric getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(
            GatlingCountMetric minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public GatlingCountMetric getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(
            GatlingCountMetric maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public GatlingCountMetric getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(
            GatlingCountMetric meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public GatlingCountMetric getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(
            GatlingCountMetric standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public GatlingCountMetric getPercentiles1() {
        return percentiles1;
    }

    public void setPercentiles1(
            GatlingCountMetric percentiles1) {
        this.percentiles1 = percentiles1;
    }

    public GatlingCountMetric getPercentiles2() {
        return percentiles2;
    }

    public void setPercentiles2(
            GatlingCountMetric percentiles2) {
        this.percentiles2 = percentiles2;
    }

    public GatlingCountMetric getPercentiles3() {
        return percentiles3;
    }

    public void setPercentiles3(
            GatlingCountMetric percentiles3) {
        this.percentiles3 = percentiles3;
    }

    public GatlingCountMetric getPercentiles4() {
        return percentiles4;
    }

    public void setPercentiles4(
            GatlingCountMetric percentiles4) {
        this.percentiles4 = percentiles4;
    }

    public GatlingPercentageMetric getGroup1() {
        return group1;
    }

    public void setGroup1(GatlingPercentageMetric group1) {
        this.group1 = group1;
    }

    public GatlingPercentageMetric getGroup2() {
        return group2;
    }

    public void setGroup2(GatlingPercentageMetric group2) {
        this.group2 = group2;
    }

    public GatlingPercentageMetric getGroup3() {
        return group3;
    }

    public void setGroup3(GatlingPercentageMetric group3) {
        this.group3 = group3;
    }

    public GatlingPercentageMetric getGroup4() {
        return group4;
    }

    public void setGroup4(GatlingPercentageMetric group4) {
        this.group4 = group4;
    }

    public GatlingCountMetric getMeanNumberOfRequestsPerSecond() {
        return meanNumberOfRequestsPerSecond;
    }

    public void setMeanNumberOfRequestsPerSecond(
            GatlingCountMetric meanNumberOfRequestsPerSecond) {
        this.meanNumberOfRequestsPerSecond = meanNumberOfRequestsPerSecond;
    }
}
