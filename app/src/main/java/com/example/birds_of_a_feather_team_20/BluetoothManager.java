package com.example.birds_of_a_feather_team_20;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

/**
 * This class is used to manage and check bluetooth status on a user's local device. Call
 * methods such as initializeBluetooth() to set up bluetooth and isBluetoothEnabled to check
 * the status of bluetooth on a user's device.
 */
public class BluetoothManager {
    MainActivity thisContext;

    public BluetoothManager(MainActivity context) {
        thisContext = context;
    }

    /**
     * Asks the user to enable bluetooth if permissions are already granted and bluetooth is off
     */
    public void initializeBluetooth() {
        // Checks if permission has been granted
        if (!new PermissionsManager(thisContext).isBluetoothPermissionGranted()) {
            Utilities.logToast(thisContext, "Bluetooth permission denied. Please turn on permission.");
            return;
        }
        // ActivityResult for bluetooth from user
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        ActivityResultLauncher<Intent> bluetoothEnableResultLauncher = thisContext.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Checks whether user granted/denied bluetooth
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Utilities.logToast(thisContext, "Bluetooth is turned on");
                    }
                    else {
                        Utilities.logToast(thisContext, "Bluetooth is off");
                    }
                });

        // Checks if Bluetooth permission has been granted
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        //             https://developer.android.com/guide/topics/connectivity/bluetooth/setup
        if (!isBluetoothEnabled()) {
            // Ask to enable bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            bluetoothEnableResultLauncher.launch(enableBtIntent);
        }
        else {
            Utilities.logToast(thisContext, "Bluetooth is on");
        }
    }

    /**
     * Checks if bluetooth is enabled/disabled
     * @return - true if enabled and false for all other cases
     */
    public boolean isBluetoothEnabled() {
        // Checks if permission has been granted
        if (new PermissionsManager(thisContext).isBluetoothPermissionGranted()) {
            return BluetoothAdapter.getDefaultAdapter().isEnabled();
        }

        Utilities.logToast(thisContext, "Bluetooth permission denied. Please turn on permission.");
        return false;
    }

}
