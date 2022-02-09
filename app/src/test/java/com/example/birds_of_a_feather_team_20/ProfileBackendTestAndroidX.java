package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ProfileBackendTestAndroidX {
//    public ActivityScenarioRule<MainActivity> scenarioRule;
//
//    @Before
//    public void setup() {
//        scenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//    }
//

    /*@Test
    public void testSucceed() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assert true;
            });
        }
    }*/

    /*@Test
    public void testFail() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assert false;
            });
        }
    }*/

    @Test
    public void testBasicProfile() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Profile bill = new Profile("Bill Clinton", "https://upload.wikimedia.org/wikipedia/commons/d/d3/Bill_Clinton.jpg", "fakeid");
        assertEquals(bill.getName(), "Bill Clinton");
        bill.setName("William Clinton");
        assertEquals(bill.getName(), "William Clinton");
    }
    @Test
    public void testBasicMyProfile() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
//                assert true;
                MyProfile.singleton(activity.getApplicationContext()).setName("George W. Bush");
                MyProfile.singleton(activity.getApplicationContext()).setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg");
                assertEquals(MyProfile.singleton(activity.getApplicationContext()).getName(), "George W. Bush");
                assertEquals(MyProfile.singleton(activity.getApplicationContext()).getPhotoURL(), "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg");
                MyProfile.singleton(activity.getApplicationContext()).setName(null);
                MyProfile.singleton(activity.getApplicationContext()).setPhotoURL(null);
            });
        }
    }
    @Test
    public void testSharedPreferences() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                // Check that "name" and "photo_url" keys haven't changed
                profile.setName("Barack Obama");
                MyProfile.singleton(activity.getApplicationContext()).setName("Barack Obama");
                assertEquals(prefs.getString("name", "default"), "Barack Obama");
                profile.setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
                assertEquals(prefs.getString("photo_url", "default"), "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
                assertEquals(profile.getPhotoURL(), "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
            });
        }
    }
    @Test
    public void testSerialize() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Profile bill = new Profile("Bill", "link", "fakeid");
        assertEquals("{\"user_id\":\"fakeid\",\"name\":\"Bill\",\"photo_url\":\"link\"}",bill.serialize());
    }

    @Test
    public void testDeserialize() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

//        Profile bill = new Profile("Bill", "link");
//        assertEquals("{\"name\":\"Bill\",\"photo_url\":\"link\"}",bill.serialize());

        Profile john = new Profile("","", "fakeid");
        john.deserialize("{\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}");
        assertEquals("John", john.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
    }
}
