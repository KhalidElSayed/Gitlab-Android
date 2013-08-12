package de.skilloverflow.gitlab.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class App {
    private App() {
    }

    private static final String MISC = "miscellaneous";

    public static void setWizardCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MISC, 0);
        prefs.edit().putBoolean("wizardCompleted", true).apply();
    }

    public static boolean isWizardCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MISC, 0);
        return prefs.getBoolean("wizardCompleted", false);
    }
}
