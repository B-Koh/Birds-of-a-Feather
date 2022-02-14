package com.example.birds_of_a_feather_team_20;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testDebugButtonCSV() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {

            });
        }
    }
}