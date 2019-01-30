package org.jenkinsci.plugins.gatlingcheck.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

/**
 * @author xiaoyao
 */
public enum MetricType {
    QPS(1, "qps");

    private int value;

    private String name;

    MetricType(int value, String name) {
        this.name = name;
        this.value = value;
    }

    @JsonCreator
    public static Optional<MetricType> findByString(String name) {
        for (MetricType item : MetricType.values()) {
            if (StringUtils.equals(item.name, name)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    public static Optional<MetricType> findByInt(int value) {
        for (MetricType item : MetricType.values()) {
            if (item.value == value) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    public int toInt() {
        return value;
    }

    @JsonValue
    @Override
    public String toString() {
        return name;
    }
}
