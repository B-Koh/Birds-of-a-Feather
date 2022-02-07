package com.example.birds_of_a_feather_team_20;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.birds_of_a_feather_team_20.bluetoothPermissions.BluetoothPermissions;

public class MainActivity extends AppCompatActivity {

    // Bluetooth fields
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_ENABLE_BT_SCAN = 101;
    private static final int REQUEST_ENABLE_BT_CONNECT = 102;

    BluetoothPermissions bt;
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Check local device permissions
        checkPermission(PERMISSIONS, REQUEST_ENABLE_BT);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            // // There are no request codes
                            // Intent data = result.getData();
                            // doSomeOperation();
                        }
                    }
        });


        BluetoothAdapter.getDefaultAdapter().disable();
        // Checks if bluetooth is disabled
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            // Ask to enable bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            someActivityResultLauncher.launch(enableBtIntent);
        }
        else {
            Toast.makeText(MainActivity.this," Bluetooth already granted", Toast.LENGTH_LONG).show();
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
                // Requests the permission denied
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

        // Loops through each permission and checks if granted
        for (int i : grantResults) {
            // A permission is denied
            if (i == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this,
                        "Bluetooth Permission required. App will not function as intended.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //ensureBluetoothEnabled();
        }





//        // Checks if bluetooth permission is enabled
//        if(requestCode == REQUEST_ENABLE_BT) {
//            // Notifies permission is granted
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, "Bluetooth Permission Granted", Toast.LENGTH_LONG).show();
//            }
//            // Notifies permission is not granted
//            else{
//                Toast.makeText(MainActivity.this, "Bluetooth Permission Denied", Toast.LENGTH_LONG).show();
//            }
//        }
//        // Checks if scan permission is enabled
//        else if (requestCode == REQUEST_ENABLE_BT_SCAN) {
//            // Notifies permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, "Bluetooth Scan Permission Granted", Toast.LENGTH_LONG).show();
//            }
//            // Notifies permission is not granted
//            else{
//                Toast.makeText(MainActivity.this, "Bluetooth Scan Permission Denied", Toast.LENGTH_LONG).show();
//            }
//        }
//        // Checks if connect permission is enabled
//        else if (requestCode == REQUEST_ENABLE_BT_CONNECT) {
//            // Notifies permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, "Bluetooth Connect Permission Granted", Toast.LENGTH_LONG).show();
//            }
//            // Notifies permission is not granted
//            else{
//                Toast.makeText(MainActivity.this, "Bluetooth Connect Permission Denied", Toast.LENGTH_LONG).show();
//            }
//        }


    }


}