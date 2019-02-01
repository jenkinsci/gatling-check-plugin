package org.jenkinsci.plugins.gatlingcheck.util;

import org.jenkinsci.plugins.gatlingcheck.data.GatlingReportStats;

import javax.annotation.Nonnull;

/**
 * @author xiaoyao
 */
public final class GatlingReportStatsUtils {

    public static double getOKRate(@Nonnull GatlingReportStats gatlingReportStats) {
        double ok = gatlingReportStats.getNumberOfRequests().getOk();
        double total = gatlingReportStats.getNumberOfRequests().getTotal();
        return ok * 100 / total;
    }

    public static double getQps(@Nonnull GatlingReportStats gatlingReportStats) {
        return gatlingReportStats.getMeanNumberOfRequestsPerSecond().getOk();
    }

    public static double getResponseTime99(@Nonnull GatlingReportStats gatlingReportStats) {
        return gatlingReportStats.getPercentiles4().getOk();
    }

    public static double getResponseTime95(@Nonnull GatlingReportStats gatlingReportStats) {
        return gatlingReportStats.getPercentiles3().getOk();
    }

    public static double getResponseTimeAvg(@Nonnull GatlingReportStats gatlingReportStats) {
        return gatlingReportStats.getMeanResponseTime().getOk();
    }

    private GatlingReportStatsUtils() {
        throw new UnsupportedOperationException();
    }
}
