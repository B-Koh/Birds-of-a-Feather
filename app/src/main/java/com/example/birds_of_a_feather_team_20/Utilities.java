package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
        Log.d(context.toString(), message);
        if (!debugToast) {
            return;
        }
        toast(context, message);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Scales down (or up) an image, maintaining the aspect ratio. The resulting dimensions will always
     * be greater than or equal to the minWidth and minHeight specified.
     */
    public static Bitmap rescaleBitmap(Bitmap original, int minWidth, int minHeight) {
        // Want the image to fill the minWidth by maxWidth frame.
        // Is the image too tall or too wide?
        float originalRatio = original.getHeight() / (float)original.getWidth();
        float targetRatio = minHeight / (float)minWidth;
        boolean tooTall = originalRatio > targetRatio;

        int resizedWidth, resizedHeight;

        // If too tall then width will be the constrained dimension, otherwise height will be
        if (tooTall) {
            resizedWidth = minWidth;
            resizedHeight = Math.round(minWidth * originalRatio);
        } else {
            resizedWidth = Math.round(minHeight / originalRatio);
            resizedHeight = minHeight;
        }
        Log.d("Resizing", "resizedWidth=" + resizedWidth + " | resizedHeight=" + resizedHeight);

        return Bitmap.createScaledBitmap(original, resizedWidth, resizedHeight, true);
    }

    public static String getFirstName(String fullName) {
        return fullName.trim().split(" ")[0];
    }
}

