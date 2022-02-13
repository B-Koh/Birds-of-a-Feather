package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;

import com.google.android.gms.nearby.messages.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class NearbyBackendTest {

    private void sendMessage(MainActivity activity, String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        activity.getNearbyManager().getProfileMessageListener().onFound(message);
        activity.getNearbyManager().getProfileMessageListener().onLost(message);
    }

    @Test
    public void testFindingPerson() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                ProfilesCollection.singleton().getProfiles().clear();
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";

                sendMessage(activity, messageStr);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                Profile john = ProfilesCollection.singleton().getProfiles().get(0);
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
                ProfilesCollection.singleton().getProfiles().clear();
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";
                String messageStr2 = "{\"user_id\":\"fakeid2\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";
                String messageStr3 = "{\"user_id\":\"fakeid3\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";

                sendMessage(activity, messageStr);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, messageStr);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, messageStr2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, messageStr);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, messageStr3);
                assertEquals(3, ProfilesCollection.singleton().getProfiles().size());
            });
        }
    }

    @Test
    public void testUpdateExisting() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                ProfilesCollection.singleton().getProfiles().clear();
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr1 = "{\"user_id\":\"fakeid\",\"name\":\"NAME1\",\"photo_url\":\"URL1\"}";
                String messageStr2 = "{\"user_id\":\"fakeid\",\"name\":\"NAME2\",\"photo_url\":\"URL2\"}";
                String messageStr3 = "{\"user_id\":\"fakeid\",\"name\":\"NAME3\",\"photo_url\":\"URL3\"}";
                Profile profile1;

                sendMessage(activity, messageStr1);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                profile1 = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("NAME1", profile1.getName());
                assertEquals("URL1", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());

                sendMessage(activity, messageStr2);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                profile1 = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("NAME2", profile1.getName());
                assertEquals("URL2", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());

                sendMessage(activity, messageStr3);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                profile1 = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("NAME3", profile1.getName());
                assertEquals("URL3", profile1.getPhotoURL());
                assertEquals("fakeid", profile1.getId());
            });
        }
    }



}
