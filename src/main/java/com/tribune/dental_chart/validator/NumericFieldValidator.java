package com.tribune.dental_chart.validator;

import java.util.regex.Pattern;

/**
 * Validator for numeric input fields in the dental chart.
 * Enforces range constraints and pattern matching for probing depth and gingival margin fields.
 */
public class NumericFieldValidator {

    private static final int MIN_VALUE = -10;
    private static final int MAX_VALUE = 10;
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[-+]?\\d{0,3}");
    public static final String MINUS_SIGN = "-";

    /**
     * Validates if a string is a valid numeric value within the allowed range.
     *
     * @param value the input value to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // Allow just a minus sign (for in-progress input)
        if (MINUS_SIGN.equals(value)) {
            return true;
        }

        // Check pattern
        if (!NUMERIC_PATTERN.matcher(value).matches()) {
            return false;
        }

        // Parse and check range
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= MIN_VALUE && intValue <= MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sanitizes an invalid input to a valid default value.
     *
     * @return "0" as default safe value
     */
    public static String getDefaultValue() {
        return "0";
    }

    /**
     * Gets the minimum allowed value.
     *
     * @return minimum value
     */
    public static int getMinValue() {
        return MIN_VALUE;
    }

    /**
     * Gets the maximum allowed value.
     *
     * @return maximum value
     */
    public static int getMaxValue() {
        return MAX_VALUE;
    }

    /**
     * Gets the validation pattern.
     *
     * @return regex pattern string
     */
    public static String getPattern() {
        return NUMERIC_PATTERN.pattern();
    }
}

