package org.jenkinsci.plugins.gatlingcheck.util;

import hudson.util.FormValidation;

/**
 * @author xiaoyao
 */
public final class FormValidationUtils {

    public static FormValidation isValidRate(String rate) {
        try {
            double value = Double.valueOf(rate);
            if (value < 0 || value > 100) {
                return FormValidation.error("Please enter a number between 0 and 100.");
            }
            return FormValidation.ok();

        } catch (NumberFormatException e) {
            return FormValidation.error("Please enter a number.");
        }
    }

    public static FormValidation isPositiveNumber(String string) {
        try {
            double value = Double.valueOf(string);
            if (value < 0) {
                return FormValidation.error("Please enter a positive number.");
            }
            return FormValidation.ok();

        } catch (NumberFormatException e) {
            return FormValidation.error("Please enter a number.");
        }
    }

    public FormValidationUtils() {
        throw new UnsupportedOperationException();
    }
}
