package com.example.birds_of_a_feather_team_20;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {
    private static final int REQUEST_ENABLE_BT = 100;
    public static final String[] PERMISSIONS = new String[]{
            //Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
    };

    MainActivity thisContext;

    public PermissionsManager(MainActivity context) {
        thisContext = context;
    }

    public void checkPermission() {
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
//                Toast.makeText(thisContext, i + " already granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onPermissionsResult(int[] grantResults) {
        // For each permission, check if user granted/denied
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_DENIED) {
                Utilities.logToast(thisContext, "Bluetooth Permission required. App will not function as intended.");
//                Toast.makeText(thisContext,"Bluetooth Permission required. App will not function as intended.", Toast.LENGTH_LONG).show();
                // TODO: For Demo, please remove after
               // findViewById(R.id.test_bluetooth_permission_granted).setVisibility(View.INVISIBLE);
                //findViewById(R.id.test_bluetooth_permission_denied).setVisibility(View.VISIBLE);

            }
            else {
                Utilities.logToast(thisContext, "Permission Granted.");
                //Toast.makeText(thisContext, "Permission Granted.", Toast.LENGTH_LONG).show();
                // TODO: For Demo, please remove after
               // findViewById(R.id.test_bluetooth_permission_granted).setVisibility(View.VISIBLE);
                //findViewById(R.id.test_bluetooth_permission_denied).setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean isBluetoothPermissionGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true;
        }
        return ContextCompat.checkSelfPermission(thisContext, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(thisContext, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
    }




}
