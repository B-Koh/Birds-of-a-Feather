package com.example.birds_of_a_feather_team_20.sorting;

import android.util.Log;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.List;

/**
 * This is a comparator which compares Profiles, prioritizing the Profiles with smaller classes
 */
public class SizeWeightComparator implements ProfileComparator {
    private final Profile myProfile;
    public static final String LABEL = "Class Size";

    public SizeWeightComparator(Profile myProfile) {
        this.myProfile = myProfile;
    }

    /**
     * Calculates the total weight of all the courses on the given Profile.
     */
    private float getWeight(Profile profile) {
        List<Course> courses = profile.matchingCourses(myProfile);
        float score = 0;

        for (Course course : courses) {
            switch (course.getClassSize()) {
                case 0: // Tiny
                    score += 1;
                    break;
                case 1: // Small
                    score += 0.33;
                    break;
                case 2: // Medium
                    score += 0.18;
                    break;
                case 3: // Large
                    score += 0.10;
                    break;
                case 4: // Huge
                    score += 0.06;
                    break;
                case 5: // Gigantic
                    score += 0.03;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + course.getClassSize());
            }
        }
        return score;
    }

    /**
     * compares the matches based on the size of the classroom
     * @param p1 first profile to compare with
     * @param p2 second profile to compare with
     * @return negative if weight of p1 > p2, equal if they are the same, positive if p1 < p2
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        // To sort highest to lowest, when p1 > p2, compare should return negative
//        return Math.round((getWeight(p2) - getWeight(p1)) * 100);
        int result = Math.round(getWeight(p2) * 100) - Math.round(getWeight(p1) * 100);
        Log.i(p1.getName() + " vs " + p2.getName(), result + "");
        return result;
    }
}
