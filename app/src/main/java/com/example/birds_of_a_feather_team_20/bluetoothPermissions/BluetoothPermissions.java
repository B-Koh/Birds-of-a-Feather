package com.example.birds_of_a_feather_team_20.bluetoothPermissions;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * This class handles Bluetooth permissions when the app is launched. It should check if Bluetooth
 * is supported, check if Bluetooth is enabled/disabled, and should request the user to enable
 * Bluetooth without leaving the app.
 */
public class BluetoothPermissions {
    private static final int REQUEST_ENABLE_BT = 10;
    BluetoothAdapter bt;

    /**
     * Constructor that sets field to local bluetooth
     */
    public BluetoothPermissions() {
        this.bt = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Checks if the local device can support bluetooth
     * @return true if device is supported and false if device is not supported
     */
    public boolean checkSupport() {
        if (this.bt == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the local device has bluetooth enabled or not
     * @return true if Bluetooth is enabled and false if Bluetooth is disabled
     */
    public boolean checkEnable() {
        // Checks if bluetooth is supported
        if (checkSupport() == false) {
            return false;
        }
        return this.bt.isEnabled();
    }

}
