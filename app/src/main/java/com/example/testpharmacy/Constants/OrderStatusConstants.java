package com.example.testpharmacy.Constants;

import android.content.Context;
import android.content.res.Resources;

import com.example.testpharmacy.R;

/**
 * This class manages the order status constants and provides translation utilities
 * between the database values and the UI display strings.
 */
public class OrderStatusConstants {
    // Database constants - DO NOT CHANGE these values as they are stored in database
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_SHIPPING = "shipping";
    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_CANCELLED = "cancelled";

    /**
     * Converts a database status value to its localized display string
     * @param context The context to access resources
     * @param status The database status value
     * @return The localized status string for display
     */
    public static String getLocalizedStatus(Context context, String status) {
        if (status == null) return "";

        Resources res = context.getResources();
        switch (status.toLowerCase()) {
            case STATUS_PENDING:
                return res.getString(R.string.status_pending);
            case STATUS_PROCESSING:
                return res.getString(R.string.status_processing);
            case STATUS_SHIPPING:
                return res.getString(R.string.status_shipping);
            case STATUS_DELIVERED:
                return res.getString(R.string.status_delivered);
            case STATUS_CANCELLED:
                return res.getString(R.string.status_cancelled);
            default:
                return status; // Return as-is if not recognized
        }
    }

    /**
     * Converts a position from the status spinner to the database status value
     * @param position The spinner position
     * @return The database status value
     */
    public static String getStatusFromPosition(int position) {
        switch (position) {
            case 0:
                return STATUS_PENDING;
            case 1:
                return STATUS_PROCESSING;
            case 2:
                return STATUS_SHIPPING;
            case 3:
                return STATUS_DELIVERED;
            case 4:
                return STATUS_CANCELLED;
            default:
                return STATUS_PENDING;
        }
    }

    /**
     * Gets the spinner position for a given database status value
     * @param status The database status value
     * @return The position in the spinner
     */
    public static int getPositionForStatus(String status) {
        if (status == null) return 0;

        switch (status.toLowerCase()) {
            case STATUS_PENDING:
                return 0;
            case STATUS_PROCESSING:
                return 1;
            case STATUS_SHIPPING:
                return 2;
            case STATUS_DELIVERED:
                return 3;
            case STATUS_CANCELLED:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * Gets the color associated with a status
     * @param status The database status value
     * @return The color value (int)
     */
    public static int getStatusColor(String status) {
        if (status == null) return 0xFF808080; // Gray for unknown

        switch (status.toLowerCase()) {
            case STATUS_PENDING:
                return 0xFF808080; // Gray
            case STATUS_PROCESSING:
                return 0xFF2196F3; // Blue
            case STATUS_SHIPPING:
                return 0xFFFF9800; // Orange
            case STATUS_DELIVERED:
                return 0xFF4CAF50; // Green
            case STATUS_CANCELLED:
                return 0xFFF44336; // Red
            default:
                return 0xFF808080; // Gray
        }
    }
}