package com.example.testpharmacy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    /**
     * Formats a number as Vietnamese Dong (VND) currency with proper thousand separators.
     * Example: 1000000 -> "1.000.000đ"
     *
     * @param amount The amount to format
     * @return Formatted string with dots as thousands separators and đ symbol
     * https://freetuts.net/thu-vien-xu-ly-du-lieu-so-trong-java-1109.html
     */
    public static String formatVND(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localeVN);
        formatter.setMaximumFractionDigits(0); // No decimal places for VND
        formatter.setMinimumFractionDigits(0);
        return formatter.format(amount);
    }
}