package com.example.birds_of_a_feather_team_20;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.sorting.MatchComparator;
import com.example.birds_of_a_feather_team_20.sorting.MatchScoreTimeWeighted;
import com.example.birds_of_a_feather_team_20.sorting.ProfileComparator;
import com.google.android.gms.nearby.messages.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(RobolectricTestRunner.class)
public class SortingTest {

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
    public void testMatchCompare() {
        Profile myProfile = new Profile("Jim", "photo", "this_is_me");
        Profile person1 = new Profile("Person1", "photo", "this_is_person1");
        Profile person2 = new Profile("Person2", "photo", "this_is_person2");
        Course course1 = new Course(2020, "FA", "CSE", "100");
        Course course2 = new Course(2021, "FA", "CSE", "100");
        Course course3 = new Course(2022, "FA", "CSE", "100");
        Course course4 = new Course(2023, "FA", "CSE", "100");

        myProfile.addCourse(course1);
        myProfile.addCourse(course2);
        myProfile.addCourse(course3);
        myProfile.addCourse(course4);

        person1.addCourse(course1);

        person2.addCourse(course2);
        person2.addCourse(course3);

        ProfileComparator comp = new MatchComparator(myProfile);

        // Person 2 has more courses matching
        assert comp.compare(person1, person2) < 0;

        person1.addCourse(course4);

        // They have the same number of matching courses
        assert comp.compare(person1, person2) == 0;

        person1.addCourse(course2);

        // Person 1 has more
        assert comp.compare(person1, person2) > 0;

        myProfile.getCourses().clear();

        // Now they each have no matching courses
        assert comp.compare(person1, person2) == 0;
    }
    @Test
    public void testMatchSort() {
        Profile myProfile = new Profile("Jim", "photo", "this_is_me");
        Profile person1 = new Profile("Person1", "photo", "this_is_person1");
        Profile person2 = new Profile("Person2", "photo", "this_is_person2");
        Course course1 = new Course(2020, "FA", "CSE", "100");
        Course course2 = new Course(2021, "FA", "CSE", "100");

        ProfileComparator comp = new MatchComparator(myProfile);

        List<Profile> profiles = new ArrayList<>();
        profiles.add(person1);
        profiles.add(person2);

        // Give person 1 the most matches
        myProfile.addCourse(course1);
        person1.addCourse(course1);

        profiles.sort(comp); // lowest to highest
        assert profiles.get(1) == person1;

        // Now give person 2 the most matches
        myProfile.addCourse(course2);
        person2.addCourse(course1);
        person2.addCourse(course2);

        profiles.sort(comp); // lowest to highest
        assert profiles.get(1) == person2;
    }
    @Test
    public void testTimeWeighted() {
        Profile person1 = new Profile("Person1", "photo", "this_is_person1");
        Profile person2 = new Profile("Person2", "photo", "this_is_person2");
        Course course1 = new Course(2020, "FA", "CSE", "100");
        Course course2 = new Course(2021, "SS", "CSE", "100");

        // Current year and quarter to compare to
        ProfileComparator comp = new MatchScoreTimeWeighted("FA", 2021);

        // Compare 1 year before current year
        person1.addCourse(course1);
        person2.addCourse(course1);
        assertEquals(2, comp.compare(person1, person2));

        // Compare 1 quarter before current quarter
        person1.addCourse(course2);
        person2.addCourse(course2);
        assertEquals(7, comp.compare(person1, person2));
    }

}
