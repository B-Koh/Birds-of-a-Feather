package com.example.birds_of_a_feather_team_20;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

public class BluetoothManager {
    MainActivity thisContext;

    public BluetoothManager(MainActivity context) {
        thisContext = context;
    }

    public void initializeBluetooth() {
        // ActivityResult for bluetooth from user
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        ActivityResultLauncher<Intent> bluetoothEnableResultLauncher = thisContext.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Checks whether user granted/denied bluetooth
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Don't do anything

                    }
                    else {
                        Utilities.logToast(thisContext, "Bluetooth is off");
                        //Toast.makeText(thisContext," Bluetooth is off", Toast.LENGTH_LONG).show();
                    }
                });

        // Checks if Bluetooth permission has been granted
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        if (new PermissionsManager(thisContext).isBluetoothPermissionGranted()) {
            // Checks if bluetooth is disabled
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                // Ask to enable bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                bluetoothEnableResultLauncher.launch(enableBtIntent);
            }
            else {
                Utilities.logToast(thisContext, "Bluetooth is on");
                //Toast.makeText(thisContext," Bluetooth is on", Toast.LENGTH_LONG).show();
            }
        }
    }
}
