package com.example.birds_of_a_feather_team_20;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * This class is used to manage and check bluetooth permissions on a user's local device. Call
 * methods such as checkPermission() to  to set up bluetooth and isBluetoothEnabled to check
 * the status of bluetooth on a user's device.
 */
public class PermissionsManager {
    private static final int REQUEST_ENABLE_BT = 100;
    public static final String[] PERMISSIONS = new String[] {
            //Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
    };

    Activity thisContext;

    public PermissionsManager(Activity context) {
        thisContext = context;
    }

    /**
     * Checks the local device's Bluetooth permissions
     * Cited Work: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
     * Cited Work: https://stackoverflow.com/questions/67722950/android-12-new-bluetooth-permissions
     * Cited Work: https://www.guidearea.com/granted-denied-and-permanently-denied-permissions-in-android/
     */
    public void checkPermission() {
        // Checks if older Android version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return;
        }

        String[] permission = PERMISSIONS;
        for (String i : permission) {
            // Requests permission if denied
            if (ContextCompat.checkSelfPermission(thisContext, i) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(thisContext, permission, REQUEST_ENABLE_BT);
            }
            // Case for when permission is granted
            else {
                Utilities.logToast(thisContext, i + " already granted");
            }
        }
    }

    /**
     * Helps handle the result of User inputs of requested permissions
     * @param grantResults - Results from user
     * Cited Work: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
     * Cited Work: https://developer.android.com/training/permissions/requesting#allow-system-manage-request-code
     */
    public void onPermissionsResult(int[] grantResults) {
        // For each permission, check if user granted/denied
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_DENIED) {
                Utilities.logToast(thisContext, "Bluetooth Permission required. App will not function as intended.");
            }
            else {
                Utilities.logToast(thisContext, "Permission Granted.");
            }
        }
    }

    /**
     * Checks if bluetooth permissions have been granted
     * @return - true if granted and false if denied
     */
    public boolean isBluetoothPermissionGranted() {
        // Checks if older Android version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true;
        }
        return ContextCompat.checkSelfPermission(thisContext, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(thisContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
    }

}
