package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.google.android.gms.nearby.messages.Message;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class NearbyBackendTest {

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

    /**
     * Create a profile, send the profile, and check that we receive the profile correctly
     */
    @Test
    public void testFindOneProfile() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("John", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                // Should not have found any profiles yet
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                sendMessage(activity, profile1);

                // Now should have exactly one profile
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                Profile john = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("John", john.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
                assertEquals("this_is_the_id", john.getId());
                reset(activity);
            });
        }
    }

    /**
     * Create a profile, send the profile, and check that we receive the profile correctly
     */
    @Test
    public void testFindNullProfile() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id");
                Profile profile2 = new Profile(null, "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id");
                Profile profile3 = new Profile("John", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", null);
                Profile profile4 = new Profile("John", null, "this_is_the_id");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course1);
                profile3.addCourse(course1);
                profile4.addCourse(course1);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course

                // Should not have found any profiles yet
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                sendMessage(activity, profile1);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile3);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile4);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                reset(activity);
            });
        }
    }
    @Test
    public void testNoMatchingCourses() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                // Construct the profile
                Profile profile1 = new Profile("John", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id");
                Course matchingCourse = new Course(2020, "FA", "CSE", "100");
                Course nonmatchingCourse = new Course(2021, "FA", "CSE", "100");
                profile1.addCourse(matchingCourse);
                sendMessage(activity, profile1);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                MyProfile.singleton(activity).addCourse(nonmatchingCourse);
                sendMessage(activity, profile1); // Adding a nonmatching course shouldn't help find that profile
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());
                MyProfile.singleton(activity).addCourse(matchingCourse); // We should have the same course
                sendMessage(activity, profile1);
                // Now should have exactly one profile
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                Profile john = ProfilesCollection.singleton().getProfiles().get(0);
                assertEquals("John", john.getName());
                assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
                assertEquals("this_is_the_id", john.getId());
                assertEquals(2020, john.getCourses().get(0).getYear());
                assertEquals("FA", john.getCourses().get(0).getSession());
                assertEquals("CSE", john.getCourses().get(0).getDepartment());
                assertEquals("100", john.getCourses().get(0).getCourseNumber());
                reset(activity);
            });
        }
    }

    @Test
    public void testFindSeveralProfiles() {
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

                sendMessage(activity, profile1);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
                sendMessage(activity, profile2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
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
    @Test
    public void testUpdateExisting() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                reset(activity);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

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


        // TESTS
    /*@Test
    public void testFindingPerson() {
        reset();
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
//                ProfilesCollection.singleton().getProfiles().clear();
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
        reset();
    }
    @Test
    public void testFindingPersonNull() {
        reset();
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
//                ProfilesCollection.singleton().getProfiles().clear();
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                Course courseInCommon = new Course(2020, "FA", "CSE", "100");
                MyProfile.singleton(activity).addCourse(courseInCommon);

                Profile profile = MyProfile.singleton(activity.getApplicationContext());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

                String messageStr = "{\"user_id\":\"fakeid\",\"name\":null,\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}";
                sendMessage(activity, messageStr);
                assertEquals(0, ProfilesCollection.singleton().getProfiles().size());

                messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"\"}";
                sendMessage(activity, messageStr);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());

                messageStr = "{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":null}";
                sendMessage(activity, messageStr);
                assertEquals(1, ProfilesCollection.singleton().getProfiles().size());
            });
        }
        reset();
    }
    @Test
    public void testFindingSeveralPeople() {
        reset();
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
//                ProfilesCollection.singleton().getProfiles().clear();
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
        reset();
    }

    @Test
    public void testUpdateExisting() {
        reset();
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
//                ProfilesCollection.singleton().getProfiles().clear();
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
        reset();
    }*/
}
