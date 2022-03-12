package com.example.birds_of_a_feather_team_20.sorting;

import android.util.Log;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.List;

/**
 * Class is used to compare courses between two users based on how recently the classes were
 * taken. Has a constructor that takes in the current quarter and current year to compare to. Use
 * compare() method
 */
public class TimeWeightComparator implements ProfileComparator {

    private final String currentSession;
    private final int currentYear;
    private final Profile myProfile;
    public static final String LABEL = "Recent";

    /**
     * Constructor
     *  @param currentSession - the current quarter
     * @param currentYear - the current year
     * @param myProfile - local profile
     */
    public TimeWeightComparator(String currentSession, int currentYear, Profile myProfile){
        this.currentSession = currentSession;
        this.currentYear = currentYear;
        this.myProfile = myProfile;
    }

    /**
     * Helper method that converts a season to a predetermined number
     *
     * @param currentSeason - season that will be converted to int
     * @return int of the season
     */
    private int convertQuarterToInt(String currentSeason) {
        switch (currentSeason) {
            case Course.FA:
                return 0;
            case Course.WI:
                return 1;
            case Course.SP:
                return 2;
            default: // summer sessions should all be 4
                return 4;
        }
    }

    /**
     * Get the total weight of the Profile's classes based on their recency.
     *
     * @param profile Profile to get classes from
     * @return the total weight
     */
    public int getWeight(Profile profile) {
        int score = 0;
        // Matching courses
        List<Course> courses = profile.matchingCourses(myProfile);

        for (Course course : courses) {

            int currentQuarter = (currentYear * 4) + convertQuarterToInt(this.currentSession);
            int courseQuarter = (course.getYear() * 4) + convertQuarterToInt(course.getSession());

            int age = currentQuarter - courseQuarter;

            int tempScore = 5 - age;

            // should be between 5 and 1
            if (tempScore > 5) tempScore = 5;
            if (tempScore < 1) tempScore = 1;

            score += tempScore;
        }

        return score;
    }

    /**
     * Compares the matches based on the recency of matched courses
     *
     * @param p1 first profile to compare with
     * @param p2 second profile to compare with
     * @return int of the score
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        // Want recent classes to come first, so compare should be negative when p1 has a higher score
        int result = getWeight(p2) - getWeight(p1);
        Log.i(p1.getName() + " vs " + p2.getName(), result + "");
        return result;
    }
}
