package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Stack;

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

//    @Test
//    public void testSerializeDeserializeCourse() {
//        Course course = new Course(2020, "FA", "CSE", "100");
//        String serialized = course.serialize();
//        Log.d("Serialized", serialized);
//        Course reconstructed = Course.deserialize(serialized);
////        assert reconstructed.equals(course);
//        assertEquals(reconstructed, course);
//    }

    @Test
    public void testSerializeDeserializeCourses() {

    }

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
        Profile bill = new Profile("Bill", "link", "fakeid");
        assertEquals("{\"user_id\":\"fakeid\",\"name\":\"Bill\",\"photo_url\":\"link\",\"course_data\":\"[]\"}",bill.serialize());
        bill.addCourse(new Course(2000, "FA", "CSE", "100"));
        bill.addCourse(new Course(2020, "WI", "CSE", "110"));
        assertEquals("{\"user_id\":\"fakeid\",\"name\":\"Bill\",\"photo_url\":\"link\",\"course_data\":\"[{\\\"course_year\\\":2000,\\\"course_session\\\":\\\"FA\\\",\\\"course_department\\\":\\\"CSE\\\",\\\"course_number\\\":\\\"100\\\",\\\"course_size\\\":0},{\\\"course_year\\\":2020,\\\"course_session\\\":\\\"WI\\\",\\\"course_department\\\":\\\"CSE\\\",\\\"course_number\\\":\\\"110\\\",\\\"course_size\\\":0}]\"}",
                bill.serialize());

        String serialized = bill.serialize();
        Profile deserialized = Profile.deserialize(serialized);
        assertEquals(2, deserialized.getCourses().size());
    }

    @Test
    public void testDeserialize() {

        Profile john = Profile.deserialize("{\"user_id\":\"fakeid\",\"name\":\"John\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}");
        assertEquals("John", john.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg", john.getPhotoURL());
        assertEquals("fakeid", john.getId());
    }

    @Test
    public void testCountMatchingCourses() {
        // TODO make sure we aren't counting null courses as the same (or just prevent null courses)
        Course CSE100_1 = new Course(2020, "WI", "CSE", "100");
        Course CSE100_2 = new Course(2020, "wi", " CSE ", "100 ");
        Profile bill = new Profile("bill", null, "fakeid");
        Profile hillary = new Profile("hillary", null, "fakeid");
        assertEquals(0, bill.countMatchingCourses(hillary));
        bill.getCourses().add(CSE100_1);
        hillary.getCourses().add(CSE100_2);
        assertEquals(1, bill.countMatchingCourses(hillary));
        assertEquals(1, hillary.countMatchingCourses(bill));
    }
}
