package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.widget.Toast;

/**
 * Helper class that helps enable/disable toasts.
 */
public class Utilities {

    public static boolean debugToast = true; // instance variable that can turn on/off toasts

    /**
     * Method helps determine whether a toast will appear depending on instance variable
     * @param context - context of where toast will appear
     * @param message - message that toast will contain
     */
    public static void logToast(Context context, String message) {
        if (!debugToast) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

