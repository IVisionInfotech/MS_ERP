package com.ivision.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public Boolean getLoginStatus() {
        return prefs.getBoolean("loginStatus", false);
    }

    public void setLoginStatus(Boolean status) {
        prefs.edit().putBoolean("loginStatus", status).apply();
    }

    public String getCurrentShipping() {
        return prefs.getString("startActivity", "");
    }

    public void setCurrentShipping(String startActivity) {
        prefs.edit().putString("startActivity", startActivity).apply();
    }

    public String getUsername() {
        return prefs.getString("username", "");
    }

    public void setUsername(String username) {
        prefs.edit().putString("username", username).apply();
    }

    public String getPassword() {
        return prefs.getString("password", "");
    }

    public void setPassword(String password) {
        prefs.edit().putString("password", password).apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        prefs.edit().putBoolean("isFirstTimeLaunch", isFirstTime).apply();
    }

    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean("isFirstTimeLaunch", true);
    }
}