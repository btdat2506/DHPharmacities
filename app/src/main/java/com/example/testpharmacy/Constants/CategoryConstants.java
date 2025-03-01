package com.example.testpharmacy.Constants;

import android.content.Context;
import android.content.res.Resources;

import com.example.testpharmacy.R;

/**
 * This class manages the product category constants and provides translation utilities
 * between the database values and the UI display strings.
 */
public class CategoryConstants {
    // Database constants - DO NOT CHANGE these values as they are stored in database
    public static final String CATEGORY_PAIN_RELIEVERS = "Pain Relievers";
    public static final String CATEGORY_ANTIBIOTICS = "Antibiotics";
    public static final String CATEGORY_VITAMINS = "Vitamins";
    public static final String CATEGORY_COLD_FLU = "Cold and Flu";
    public static final String CATEGORY_FIRST_AID = "First Aid";

    /**
     * Converts a database category value to its localized display string
     * @param context The context to access resources
     * @param category The database category value
     * @return The localized category string for display
     */
    public static String getLocalizedCategory(Context context, String category) {
        if (category == null) return "";

        Resources res = context.getResources();
        switch (category) {
            case CATEGORY_PAIN_RELIEVERS:
                return res.getString(R.string.category_pain_relievers);
            case CATEGORY_ANTIBIOTICS:
                return res.getString(R.string.category_antibiotics);
            case CATEGORY_VITAMINS:
                return res.getString(R.string.category_vitamins);
            case CATEGORY_COLD_FLU:
                return res.getString(R.string.category_cold_flu);
            case CATEGORY_FIRST_AID:
                return res.getString(R.string.category_first_aid);
            default:
                return category; // Return as-is if not recognized
        }
    }

    /**
     * Converts a localized display string to its database category value
     * @param context The context to access resources
     * @param localizedCategory The localized category string
     * @return The database category value
     */
    public static String getDatabaseCategory(Context context, String localizedCategory) {
        Resources res = context.getResources();

        if (localizedCategory.equals(res.getString(R.string.category_pain_relievers))) {
            return CATEGORY_PAIN_RELIEVERS;
        } else if (localizedCategory.equals(res.getString(R.string.category_antibiotics))) {
            return CATEGORY_ANTIBIOTICS;
        } else if (localizedCategory.equals(res.getString(R.string.category_vitamins))) {
            return CATEGORY_VITAMINS;
        } else if (localizedCategory.equals(res.getString(R.string.category_cold_flu))) {
            return CATEGORY_COLD_FLU;
        } else if (localizedCategory.equals(res.getString(R.string.category_first_aid))) {
            return CATEGORY_FIRST_AID;
        } else {
            return localizedCategory; // Return as-is if not recognized
        }
    }

    /**
     * Get all database category values
     * @return Array of database category values
     */
    public static String[] getAllDatabaseCategories() {
        return new String[] {
                CATEGORY_PAIN_RELIEVERS,
                CATEGORY_ANTIBIOTICS,
                CATEGORY_VITAMINS,
                CATEGORY_COLD_FLU,
                CATEGORY_FIRST_AID
        };
    }
}