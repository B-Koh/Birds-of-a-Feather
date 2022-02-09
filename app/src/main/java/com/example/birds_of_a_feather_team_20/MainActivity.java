package com.example.birds_of_a_feather_team_20;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Bluetooth fields
    private static final int REQUEST_ENABLE_BT = 100;
    private static final String[] PERMISSIONS = new String[]{
            //Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            //Manifest.permission.BLUETOOTH_CONNECT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Checks Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(PERMISSIONS, REQUEST_ENABLE_BT);
        }

        // ActivityResult for bluetooth from user
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Checks whether user granted/denied bluetooth
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Don't do anything
                    }
                    else {
                        Toast.makeText(MainActivity.this," Bluetooth is off", Toast.LENGTH_LONG).show();
                    }
                });

        // Checks if Bluetooth permission has been granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            // Checks if bluetooth is disabled
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                // Ask to enable bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                someActivityResultLauncher.launch(enableBtIntent);
            }
            else {
                Toast.makeText(MainActivity.this," Bluetooth is on", Toast.LENGTH_LONG).show();
            }
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");
        MyProfile.singleton(getApplicationContext());
    }


    /**
     * Checks the local device's permission for passed in permission
     * @param permission - the permission that is being checked
     * @param requestCode - the request code associated with permission
     */
    public void checkPermission(String[] permission, int requestCode) {
        for (String i : permission) {
            // Requests permission if denied
            if (ContextCompat.checkSelfPermission(MainActivity.this, i) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, permission, requestCode);
            }
            // Case for when permission is granted
            else {
                Toast.makeText(MainActivity.this, i + " already granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Handles result of User input
     * @param requestCode - Code unique to enabling Bluetooth permissions
     * @param permissions - Bluetooth permissions that are being checked
     * @param grantResults - Results from user
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ENABLE_BT) {
            // For each permission, check if user granted/denied
            for (int i : grantResults) {
                if (i == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(MainActivity.this,"Bluetooth Permission required. App will not function as intended.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Permission Granted.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}