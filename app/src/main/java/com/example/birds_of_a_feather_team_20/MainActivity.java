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

        // Check Android version for bt
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Checks if permission has not been granted
            checkPermission(PERMISSIONS, REQUEST_ENABLE_BT);
        }

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        // Intent data = result.getData();
                    }
                    else {
                        Toast.makeText(MainActivity.this," Bluetooth is off", Toast.LENGTH_LONG).show();
                    }
                });

        // Checks if Bluetooth has been granted
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
            // Checks if permission is denied
            if (ContextCompat.checkSelfPermission(MainActivity.this, i) == PackageManager.PERMISSION_DENIED) {
                // Asks for permission
                ActivityCompat.requestPermissions(MainActivity.this, permission, requestCode);
            }
            // Case for when permission is granted
            else {
                // Feedback
                Toast.makeText(MainActivity.this, i + " already granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ENABLE_BT) {
            // For each permission, check if user granted/denied
            for (int i : grantResults) {
                // Checks if permission was denied
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