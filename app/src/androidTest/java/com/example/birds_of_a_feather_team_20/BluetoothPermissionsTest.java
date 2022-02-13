package com.example.birds_of_a_feather_team_20;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

import java.util.Collection;
import java.util.concurrent.Executors;

/**
 * Test class that helps check Bluetooth permissions and check Bluetooth status.
 */
@RunWith(AndroidJUnit4.class)
public class BluetoothPermissionsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests if bluetooth permissions have been granted on local device
     */
    @Test
    public void bluetoothPermissionsTest() {
        Utilities.debugToast = false;

        activityScenarioRule.getScenario().onActivity(activity -> {
            GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_SCAN);
            GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_CONNECT);

            PermissionsManager pm = new PermissionsManager(activity);
            pm.checkPermission();
            assertTrue(pm.isBluetoothPermissionGranted());
        });
    }

    /**
     * Tests if bluetooth has been enabled on local device.
     * Note: Will fail if bluetooth is not enabled on testing device first!
     */
    @Test
    public void isBluetoothEnabledTest()  {
        Utilities.debugToast = false;
        activityScenarioRule.getScenario().onActivity(activity -> {
            GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_SCAN);
            GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_CONNECT);

            BluetoothManager bt = new BluetoothManager(activity);
            assertTrue(bt.isBluetoothEnabled());
        });
    }
}


