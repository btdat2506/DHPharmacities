// UserSessionManager.java
package com.example.testpharmacy.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";

    private static final String KEY_IS_ADMIN = "isAdmin";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Singleton instance
    private static UserSessionManager instance;

    private UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized UserSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId);
        editor.commit();
    }

    public long getUserId() {
        return pref.getLong(KEY_USER_ID, -1);
    }

    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void setAdmin(boolean isAdmin) {
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.commit();
    }

    public boolean isAdmin() {
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }
}