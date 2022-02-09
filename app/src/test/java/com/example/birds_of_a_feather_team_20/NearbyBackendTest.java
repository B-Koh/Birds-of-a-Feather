package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class NearbyBackendTest {

    private void sendMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        NearbyManager.getMessageListener().onFound(message);
        NearbyManager.getMessageListener().onLost(message);
    }

    @Test
    public void testFindingPerson() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                NearbyManager.nearbyProfiles.clear();
                assertEquals(0, NearbyManager.nearbyProfiles.size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";

                sendMessage(messageStr);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                Profile john = NearbyManager.nearbyProfiles.get(0);
                assertEquals("John", john.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
                assertEquals("fakeid", john.getId());
            });
        }
    }
    @Test
    public void testFindingSeveralPeople() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                NearbyManager.nearbyProfiles.clear();
                assertEquals(0, NearbyManager.nearbyProfiles.size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";
                String messageStr2 = "{\"user_id\":\"fakeid2\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";
                String messageStr3 = "{\"user_id\":\"fakeid3\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";

                sendMessage(messageStr);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                sendMessage(messageStr);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                sendMessage(messageStr2);
                assertEquals(2, NearbyManager.nearbyProfiles.size());
                sendMessage(messageStr);
                assertEquals(2, NearbyManager.nearbyProfiles.size());
                sendMessage(messageStr3);
                assertEquals(3, NearbyManager.nearbyProfiles.size());
            });
        }
    }
    @Test
    public void testUpdateExisting() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                NearbyManager.nearbyProfiles.clear();
                assertEquals(0, NearbyManager.nearbyProfiles.size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr1 = "{\"user_id\":\"fakeid\",\"name\":\"NAME1\",\"photo_url\":\"URL1\"}";
                String messageStr2 = "{\"user_id\":\"fakeid\",\"name\":\"NAME2\",\"photo_url\":\"URL2\"}";
                String messageStr3 = "{\"user_id\":\"fakeid\",\"name\":\"NAME3\",\"photo_url\":\"URL3\"}";
                Profile profile1;

                sendMessage(messageStr1);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                profile1 = NearbyManager.nearbyProfiles.get(0);
                assertEquals("NAME1", profile1.getName());
                assertEquals("URL1", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());

                sendMessage(messageStr2);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                profile1 = NearbyManager.nearbyProfiles.get(0);
                assertEquals("NAME2", profile1.getName());
                assertEquals("URL2", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());

                sendMessage(messageStr3);
                assertEquals(1, NearbyManager.nearbyProfiles.size());
                profile1 = NearbyManager.nearbyProfiles.get(0);
                assertEquals("NAME3", profile1.getName());
                assertEquals("URL3", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());
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

        Profile john = new Profile("","", "");
        john.deserialize("{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}");
        assertEquals("John", john.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
        assertEquals("fakeid", john.getId());
    }
}
