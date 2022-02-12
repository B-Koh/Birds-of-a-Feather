package com.example.birds_of_a_feather_team_20;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.Manifest;
import android.content.pm.PackageManager;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BluetoothPermissionsGrantedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);




    @Test
    public void bluetoothPermissionsWithBluetoothTest() {
        //GrantPermissionRule.grant("android.permission.BLUETOOTH_SCAN");




        assertTrue(ContextCompat.checkSelfPermission(InstrumentationRegistry.getInstrumentation().getContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED);

//        ViewInteraction textView = onView(
//                allOf(withId(R.id.test_bluetooth_permission_granted), withText("DEMO: Bluetooth permission has been granted"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView.check(matches(withText("DEMO: Bluetooth permission has been granted")));
    }
}
