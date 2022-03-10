package com.example.birds_of_a_feather_team_20;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;

@RunWith(RobolectricTestRunner.class)
public class FavoriteTest {

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
    public void testFavorite() {
        Profile person1 = new Profile("Person1", "photo", "this_is_person1");
        Profile person2 = new Profile("Person2", "photo", "this_is_person2");
        Profile person3 = new Profile("Person3", "photo", "this_is_person3");

        person1.setFavorite();
        assert person1.getIsFavorite();


        assert !person2.getIsFavorite();
        person2.setFavorite();
        assert person2.getIsFavorite();


        person3.setFavorite();
        assert person3.getIsFavorite();
        person3.unFavorite();
        assert !person3.getIsFavorite();

    }


    @Test
    public void testNumberOfFavorites() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {

                // Start Nearby
                activity.getNearbyManager().startScanning();

                // Construct the profile
                Profile profile1 = new Profile("John", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "new_id");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                Profile profile2 = new Profile("John1", "John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id");
                Course course2 = new Course(2021, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course2);
                MyProfile.singleton(activity).addCourse(course1); // We should have the same course
                MyProfile.singleton(activity).addCourse(course2);
                //profile1.setFavorite();

                sendMessage(activity, profile1);
                sendMessage(activity, profile2);

                //profile1.setFavorite();
                ProfilesCollection.singleton().getProfiles().get(0).setFavorite();
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
                assertEquals(1, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                ProfilesCollection.singleton().getProfiles().get(1).setFavorite();
                assertEquals(2, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                ProfilesCollection.singleton().getProfiles().get(1).unFavorite();
                assertEquals(1, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                reset(activity);
            });
        }
    }




}
