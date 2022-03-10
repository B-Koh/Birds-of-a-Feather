package com.example.birds_of_a_feather_team_20;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;

import com.example.birds_of_a_feather_team_20.model.db.Course;

@RunWith(RobolectricTestRunner.class)
public class FavoriteTest {

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

                ProfilesCollection.singleton().getProfiles().clear();

                // Construct the profile
                Profile profile1 = new Profile("John1", "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id1");
                Course course1 = new Course(2020, "FA", "CSE", "100");
                Profile profile2 = new Profile("John2", "John_F._Kennedy,_White_House_color_photo_portrait.jpg", "this_is_the_id2");
                Course course2 = new Course(2021, "FA", "CSE", "100");
                profile1.addCourse(course1);
                profile2.addCourse(course2);

                // Set favorite before adding to collection
                profile1.setFavorite();
                ProfilesCollection.singleton().getProfiles().add(profile1);
                ProfilesCollection.singleton().getProfiles().add(profile2);
                assertEquals(2, ProfilesCollection.singleton().getProfiles().size());
                assertEquals(1, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                // Set favorite after adding to collection
                profile2.setFavorite();
                assertEquals(2, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                // Check size when unfavorited
                ProfilesCollection.singleton().getProfiles().get(1).unFavorite();
                assertEquals(1, new FavoriteListView(activity).filterFavorites(ProfilesCollection.singleton().getProfiles()).size());

                ProfilesCollection.singleton().getProfiles().clear();
            });
        }
    }

}
