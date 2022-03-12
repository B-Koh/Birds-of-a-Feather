package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.sorting.SizeWeightComparator;
import com.example.birds_of_a_feather_team_20.sorting.TimeWeightComparator;
import com.google.android.gms.nearby.messages.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class ProfilesCollectionTest {

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
    public void testAddSameId() {

        ProfilesCollection coll = ProfilesCollection.singleton();
        coll.getProfiles().clear();
        assertEquals(coll.getProfiles().size(), 0);
        coll.addOrUpdateProfile(new Profile("John", "url", "id"));
        assertEquals(coll.getProfiles().size(), 1);
        coll.addOrUpdateProfile(new Profile("John1", "url", "id"));
        assertEquals(coll.getProfiles().size(), 1);
    }

    @Test
    public void applySort() {
        ProfilesCollection coll = ProfilesCollection.singleton();
        coll.getProfiles().clear();
        Profile p1 = new Profile("John", "url", "id");
        Profile p2 = new Profile("John2", "url", "id2");
        Profile myProfile = new Profile("me", "url", "my_id");

        Course smallOldCourse = new Course(2020, "FA", "CSE", "100");
        smallOldCourse.setClassSize(0);
        Course hugeRecentCourse = new Course(2022, "FA", "MATH", "18");
        hugeRecentCourse.setClassSize(4);

        p1.addCourse(smallOldCourse);
        p2.addCourse(hugeRecentCourse);
        myProfile.addCourse(smallOldCourse);
        myProfile.addCourse(hugeRecentCourse);

        // prioritize small courses first
        coll.changeSort(new SizeWeightComparator(myProfile));

        // Insert the profiles
        coll.addOrUpdateProfile(p1);
        coll.addOrUpdateProfile(p2);

        // Should be 2 profiles
        assertEquals(coll.getProfiles().size(), 2);

        // p1 should come first, since it has a small course
        assertEquals(p1, coll.getProfiles().get(0));

        // now prioritize recent courses
        coll.changeSort(new TimeWeightComparator("WI", 2022, myProfile));

        // so p2 should come first now
        assertEquals(p2, coll.getProfiles().get(0));

    }

    @Test
    public void testUpdateExisting() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Start Nearby
                activity.getNearbyManager().startScanning();
                // Note that these have the same ID
                Profile profile1 = new Profile("Joe Biden", "https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg", "id_here");
                Profile profile2 = new Profile("Donald Trump", "https://upload.wikimedia.org/wikipedia/commons/5/56/Donald_Trump_official_portrait.jpg", "id_here");
                Profile profile3 = new Profile("Barack Obama", "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", "id_here");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course1);
                profile3.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                Profile currentProfile;

                sendMessage(activity, profile1);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                currentProfile = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("Joe Biden", currentProfile.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/6/68/Joe_Biden_presidential_portrait.jpg", currentProfile.getPhotoURL());
                assertEquals("id_here", currentProfile.getId());

                sendMessage(activity, profile2);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                currentProfile = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("Donald Trump", currentProfile.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/5/56/Donald_Trump_official_portrait.jpg", currentProfile.getPhotoURL());
                assertEquals("id_here", currentProfile.getId());

                sendMessage(activity, profile3);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                currentProfile = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("Barack Obama", currentProfile.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg", currentProfile.getPhotoURL());
                assertEquals("id_here", currentProfile.getId());
                reset(activity);
            });
        }
    }
}
