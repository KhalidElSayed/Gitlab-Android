package de.skilloverflow.gitlab.api;

import android.content.Context;
import android.content.SharedPreferences;

public class CredentialsProvider {
    private CredentialsProvider() {
    }

    private static final String CREDENTIALS = "credentials";

    public static void setBaseUrl(Context context, String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url += "/api/v3";
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("url", url).apply();
    }

    public static void setPrivateToken(Context context, String privateToken) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("privateToken", privateToken).apply();
    }

    public static String getBaseUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        return prefs.getString("url", "");
    }

    public static String getPrivateToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        return prefs.getString("privateToken", "");
    }
}
