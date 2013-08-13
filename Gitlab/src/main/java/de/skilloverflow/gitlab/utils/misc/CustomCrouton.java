package de.skilloverflow.gitlab.utils.misc;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import de.keyboardsurfer.android.widget.crouton.Style.Builder;

public class CustomCrouton {
    public static final Style ALERT;
    public static final Style CONFIRM;
    public static final Style INFO;

    private static final int HOLO_RED_LIGHT = 0xffff4444;
    private static final int HOLO_GREEN_LIGHT = 0xff99cc00;
    private static final int HOLO_BLUE_LIGHT = 0xff33b5e5;

    private static final int LONG = 2500;
    private static final int MEDIUM = 1500;

    static {
        ALERT = new Builder().setDuration(LONG).setBackgroundColorValue(HOLO_RED_LIGHT).setHeight(LayoutParams.WRAP_CONTENT).build();
        CONFIRM = new Builder().setDuration(MEDIUM).setBackgroundColorValue(HOLO_GREEN_LIGHT).setHeight(LayoutParams.WRAP_CONTENT).build();
        INFO = new Builder().setDuration(MEDIUM).setBackgroundColorValue(HOLO_BLUE_LIGHT).setHeight(LayoutParams.WRAP_CONTENT).build();
    }

    /**
     * Show a Crouton, if the {@link de.keyboardsurfer.android.widget.crouton.Crouton} throws an Exception,
     * a {@link android.widget.Toast} with the same message get displayed
     *
     * @param activity The activity where the Crouton should be displayed
     * @param text     The text for the Crouton or Toast
     * @param style    The Crouton style
     */
    public static void show(Activity activity, String text, Style style) {
        try {
            Crouton.showText(activity, text, style);

        } catch (Exception e) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show a Crouton, if the {@link de.keyboardsurfer.android.widget.crouton.Crouton} throws an Exception,
     * a {@link android.widget.Toast} with the same message get displayed
     *
     * @param activity The activity where the Crouton should be displayed
     * @param text     The textID for the Crouton or Toast
     * @param style    The Crouton style
     */
    public static void show(Activity activity, int text, Style style) {
        try {
            Crouton.showText(activity, activity.getResources().getString(text), style);

        } catch (Exception e) {
            Toast.makeText(activity, activity.getResources().getString(text), Toast.LENGTH_SHORT).show();
        }
    }
}
