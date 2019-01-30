package org.jenkinsci.plugins.gatlingcheck.data.metric;

/**
 * @author xiaoyao
 */
public class GatlingCountMetric {

    private double total;

    private double ok;

    private double ko;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getOk() {
        return ok;
    }

    public void setOk(double ok) {
        this.ok = ok;
    }

    public double getKo() {
        return ko;
    }

    public void setKo(double ko) {
        this.ko = ko;
    }
}
