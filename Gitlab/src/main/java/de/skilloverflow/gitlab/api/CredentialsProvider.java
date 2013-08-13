package de.skilloverflow.gitlab.api;

import android.content.Context;
import android.content.SharedPreferences;

public class CredentialsProvider {
    private CredentialsProvider() {
    }

    private static final String CREDENTIALS = "credentials";

    /**
     * Saves the URL for accessing the API.
     *
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @param url     The base URL for the Gitlab instance (e.g http://demo.gitlab.com).
     */
    public static void setUrl(Context context, String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url += "/api/v3";
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("url", url).apply();
    }

    /**
     * Saves the private token of the user for accessing the API.
     *
     * @param context      A Context for accessing the {@link SharedPreferences}.
     * @param privateToken The private token, acquired with the POST /session call.
     */
    public static void setPrivateToken(Context context, String privateToken) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("privateToken", privateToken).apply();
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The URL for API access.
     */
    public static String getUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        return prefs.getString("url", "");
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The private token for API access.
     */
    public static String getPrivateToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        return prefs.getString("privateToken", "");
    }
}
