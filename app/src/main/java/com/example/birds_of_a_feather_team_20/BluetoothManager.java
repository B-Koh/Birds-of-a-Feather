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
    private static final int REQUEST_ENABLE_BT = 100;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
    };

    MainActivity thisContext;

    public BluetoothManager(MainActivity context) {
        thisContext = context;
    }

    public void initializeBluetooth() {
        // ActivityResult for bluetooth from user
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        ActivityResultLauncher<Intent> BluetoothEnableResultLauncher = thisContext.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Checks whether user granted/denied bluetooth
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Don't do anything

                    }
                    else {
                        Toast.makeText(thisContext," Bluetooth is off", Toast.LENGTH_LONG).show();
                    }
                });
//
        // Checks if Bluetooth permission has been granted
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED ) {
            // Checks if bluetooth is disabled
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                // Ask to enable bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                BluetoothEnableResultLauncher.launch(enableBtIntent);
            }
            else {
                Toast.makeText(thisContext," Bluetooth is on", Toast.LENGTH_LONG).show();
            }
        }
    }
}
