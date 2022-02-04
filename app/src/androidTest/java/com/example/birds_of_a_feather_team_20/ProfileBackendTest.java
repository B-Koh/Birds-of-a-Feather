package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProfileBackendTest {
    @Test
    public void testBasicProfile() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Profile bill = new Profile("Bill Clinton", "https://upload.wikimedia.org/wikipedia/commons/d/d3/Bill_Clinton.jpg");
        assertEquals(bill.getName(), "Bill Clinton");
        bill.setName("William Clinton");
        assertEquals(bill.getName(), "William Clinton");
    }
    @Test
    public void testBasicMyProfile() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MyProfile.singleton(appContext).setName("George W. Bush");
        MyProfile.singleton(appContext).setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg");
        assertEquals(MyProfile.singleton(appContext).getName(), "George W. Bush");
        assertEquals(MyProfile.singleton(appContext).getPhotoURL(), "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg");
    }
    @Test
    public void testSharedPreferences() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Profile profile = MyProfile.singleton(appContext);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        // Check that "name" and "photo_url" keys haven't changed
        assertEquals(profile.getName(), prefs.getString("name", null));
        profile.setName("Barack Obama");
        assertEquals(prefs.getString("name", "default"), "Barack Obama");
        profile.setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
        assertEquals(prefs.getString("photo_url", "default"), "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
        assertEquals(profile.getPhotoURL(), "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg");
    }
}