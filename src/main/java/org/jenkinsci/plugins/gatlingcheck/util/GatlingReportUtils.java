package org.jenkinsci.plugins.gatlingcheck.util;

import org.jenkinsci.plugins.gatlingcheck.data.GatlingReport;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.MapUtils.isEmpty;

/**
 * @author xiaoyao
 */
public final class GatlingReportUtils {

    public static double getQps(@Nonnull GatlingReport gatlingReport) {
        return GatlingReportStatsUtils.getQps(gatlingReport.getStats());
    }

    public static double getResponseTime99(@Nonnull GatlingReport gatlingReport) {
        return GatlingReportStatsUtils.getResponseTime99(gatlingReport.getStats());
    }

    public static double getResponseTime95(@Nonnull GatlingReport gatlingReport) {
        return GatlingReportStatsUtils.getResponseTime95(gatlingReport.getStats());
    }

    public static double getResponseTimeAvg(@Nonnull GatlingReport gatlingReport) {
        return GatlingReportStatsUtils.getResponseTimeAvg(gatlingReport.getStats());
    }

    public static boolean isRequestReport(@Nonnull GatlingReport gatlingReport) {
        return "REQUEST".equals(gatlingReport.getType());
    }

    public static List<String> getRequestNames(@Nonnull GatlingReport gatlingReport) {
        if (isEmpty(gatlingReport.getContents())) {
            return emptyList();
        }
        return gatlingReport.getContents().values().stream()
                .filter(GatlingReportUtils::isRequestReport)
                .map(GatlingReport::getName)
                .collect(toList());
    }

    public static Optional<GatlingReport> getRequestReportByName(
            @Nonnull GatlingReport gatlingReport, @Nonnull String name
    ) {
        return isEmpty(gatlingReport.getContents())
                ? empty()
                : gatlingReport.getContents().values().stream()
                .filter(content -> name.equals(content.getName()))
                .findFirst();
    }

    private GatlingReportUtils() {
        throw new UnsupportedOperationException();
    }
}
