package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExampleRobolectricTest {
//    public ActivityScenarioRule<MainActivity> scenarioRule;
//
//    @Before
//    public void setup() {
//        scenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//    }
//

    @Test
    public void testSucceed() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assert true;
            });
        }
    }

    @Test
    public void testFail() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assert false;
            });
        }
    }
}
