package com.example.birds_of_a_feather_team_20;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.Manifest;
import android.app.Activity;

import java.util.Collection;

/**
 * Test class that helps check Bluetooth permissions and check Bluetooth status.
 */
public class BluetoothPermissionsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests if bluetooth permissions have been granted on local device
     * Cited Work: https://stackoverflow.com/questions/50403128/how-to-grant-permissions-to-android-instrumented-tests
     */
    @Test
    public void bluetoothPermissionsTest() {
        Utilities.debugToast = false;
        Activity activity = getCurrentActivity();

        GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_SCAN);

        PermissionsManager pm = new PermissionsManager(activity);
        pm.checkPermission();

        assertTrue(pm.isBluetoothPermissionGranted());
    }

    /**
     * Tests if bluetooth has been enabled on local device.
     * Note: Will fail if bluetooth is not enabled on local device first!
     */
    @Test
    public void isBluetoothEnabledTest() {
        Utilities.debugToast = false;
        MainActivity activity = (MainActivity) getCurrentActivity();

        BluetoothManager bt = new BluetoothManager(activity);
        assertTrue(bt.isBluetoothEnabled());
    }

    /**
     * Helper method to obtain MainActivity
     * Cited Work: https://stackoverflow.com/a/53023272
     */
    public static Activity getCurrentActivity() {
        final Activity[] currentActivity = {null};
        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()) {
                currentActivity[0] = resumedActivities.iterator().next();
            }
        });
        return currentActivity[0];
    }

}


