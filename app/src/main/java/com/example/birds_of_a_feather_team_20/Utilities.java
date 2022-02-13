package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.widget.Toast;

public class Utilities {

    public static final boolean debugToast = false;

    public static void logToast(Context context, String message) {
        if (!debugToast) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}