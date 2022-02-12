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
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Bluetooth fields
    private static final int REQUEST_ENABLE_BT = 100;
    private static final String[] PERMISSIONS = new String[]{
            //Manifest.permission.BLUETOOTH,
            //Manifest.permission.BLUETOOTH_SCAN,
            //Manifest.permission.BLUETOOTH_CONNECT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");
        MyProfile.singleton(getApplicationContext());


        // Checks Bluetooth Permissions on startup
        // bluetoothStart();

    }

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void bluetoothStart() {
        // TODO: Follow SRP on Bluetooth Permissions and Bluetooth Adapter and
        //       refactor in MS2

        // Checks Android version
        // Cited Work:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(PERMISSIONS, REQUEST_ENABLE_BT);
        }



        // ActivityResult for bluetooth from user
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        ActivityResultLauncher<Intent> BluetoothEnableResultLauncher = registerForActivityResult(
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
        // Cited Work: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            // Checks if bluetooth is disabled
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                // Ask to enable bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                BluetoothEnableResultLauncher.launch(enableBtIntent);
            }
            else {
                Toast.makeText(MainActivity.this," Bluetooth is on", Toast.LENGTH_LONG).show();
            }
        }

    }


    /**
     * Helper method that checks the local device's permission for passed in permission
     * @param permission - the permission that is being checked
     * @param requestCode - the request code associated with permission
     * Cited Work: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
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
                //TODO TEST
                findViewById(R.id.test_bluetooth_permission_granted).setVisibility(View.VISIBLE);
                findViewById(R.id.test_bluetooth_permission_denied).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Handles result of User input of requested permissions
     * @param requestCode - Code unique to enabling Bluetooth permissions
     * @param permissions - Bluetooth permissions that are being checked
     * @param grantResults - Results from user
 * Cited Work: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check request code
        if (requestCode == REQUEST_ENABLE_BT) {
            // For each permission, check if user granted/denied
            for (int i : grantResults) {
                if (i == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(MainActivity.this,"Bluetooth Permission required. App will not function as intended.", Toast.LENGTH_LONG).show();
                    // TODO: For Demo, please remove after
                    findViewById(R.id.test_bluetooth_permission_granted).setVisibility(View.INVISIBLE);
                    findViewById(R.id.test_bluetooth_permission_denied).setVisibility(View.VISIBLE);

                }
                else {
                    Toast.makeText(MainActivity.this, "Permission Granted.", Toast.LENGTH_LONG).show();
                    // TODO: For Demo, please remove after
                    findViewById(R.id.test_bluetooth_permission_granted).setVisibility(View.VISIBLE);
                    findViewById(R.id.test_bluetooth_permission_denied).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}