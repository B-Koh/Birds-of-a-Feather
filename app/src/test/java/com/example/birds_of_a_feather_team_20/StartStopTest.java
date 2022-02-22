package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.google.android.gms.nearby.messages.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class StartStopTest {

    // HELPER METHODS
    private void sendMessage(MainActivity activity, String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        activity.getNearbyManager().getProfileMessageListener().onFound(message);
        activity.getNearbyManager().getProfileMessageListener().onLost(message);
    }
    private void sendMessage(MainActivity activity, Profile profile) {
        String msg = profile.serialize();
        sendMessage(activity, msg);
    }
    private void reset(Context context) {
        ProfilesCollection.singleton().getProfiles().clear();
        MyProfile.singleton(context).getCourses().clear();
    }

    @Test
    public void testNotStarted() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("Joe Biden", "https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg", "id_biden");
                Profile profile2 = new Profile("Donald Trump", "https://upload.wikimedia.org/wikipedia/commons/5/56/Donald_Trump_official_portrait.jpg", "id_trump");
                Profile profile3 = new Profile("Barack Obama", "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", "id_obama");
                Profile profile4 = new Profile("George Bush", "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg", "id_bush");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course1);
                profile3.addCourse(course1);
                profile4.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                // Should not have found any profiles yet
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Nearby is not started, so adding profiles should not work.
                sendMessage(activity, profile1);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

            });
        }
    }
    @Test
    public void testStarting() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("Joe Biden", "https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg", "id_biden");
                Profile profile2 = new Profile("Donald Trump", "https://upload.wikimedia.org/wikipedia/commons/5/56/Donald_Trump_official_portrait.jpg", "id_trump");
                Profile profile3 = new Profile("Barack Obama", "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", "id_obama");
                Profile profile4 = new Profile("George Bush", "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg", "id_bush");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course1);
                profile3.addCourse(course1);
                profile4.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                // Should not have found any profiles yet
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Nearby is not started, so adding profiles should not work.
                sendMessage(activity, profile1);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Start Nearby
                activity.getNearbyManager().startScanning();
                sendMessage(activity, profile1);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
            });
        }
    }
    @Test
    public void testRestarting() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("Joe Biden", "https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg", "id_biden");
                Profile profile2 = new Profile("Donald Trump", "https://upload.wikimedia.org/wikipedia/commons/5/56/Donald_Trump_official_portrait.jpg", "id_trump");
                Profile profile3 = new Profile("Barack Obama", "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", "id_obama");
                Profile profile4 = new Profile("George Bush", "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg", "id_bush");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course1);
                profile3.addCourse(course1);
                profile4.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                // Should not have found any profiles yet
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Nearby is not started, so adding profiles should not work.
                sendMessage(activity, profile1);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Start Nearby
                activity.getNearbyManager().startScanning();
                sendMessage(activity, profile1);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());

                // Stop Nearby
                sendMessage(activity, profile1);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());


                // Start Nearby
                activity.getNearbyManager().startScanning();

                sendMessage(activity, profile3);
                assertEquals(3, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile4);
                assertEquals(4, ProfilesCollection.singleton().getProfiles().size());

                assertEquals("Joe Biden", ProfilesCollection.singleton().getProfiles().get(0).getName());
                assertEquals("Donald Trump", ProfilesCollection.singleton().getProfiles().get(1).getName());
                assertEquals("Barack Obama", ProfilesCollection.singleton().getProfiles().get(2).getName());
                assertEquals("George Bush", ProfilesCollection.singleton().getProfiles().get(3).getName());
                reset(activity);
            });
        }
    }
}
