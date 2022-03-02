package com.example.birds_of_a_feather_team_20.sorting;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.List;

/**
 * Class is used to compare courses between two users based on how recent the classes were
 * taken. Has a constructor that takes in the current quarter and current year to compare to. Use
 * compare() method
 */
public class MatchScoreTimeWeighted implements ProfileComparator {

    private final String currentSession;
    private final int currentYear;

    /**
     * Constructor
     *
     * @param currentSession - the current quarter
     * @param currentYear - the current year
     */
    public MatchScoreTimeWeighted(String currentSession, int currentYear){
        this.currentSession = currentSession;
        this.currentYear = currentYear;
    }

    /**
     * Helper method that converts a season to a predetermined number
     *
     * @param currentSeason - season that will be converted to int
     * @return int of the season
     */
    private int convertSessionToInt(String currentSeason) {
        switch (currentSeason) {
            case "Winter":
                return 1;
            case "Spring":
                return 2;
            case "Summer":
                return 3;
        }
        return 4;
    }


    /**
     * Compares the matches based on the size of the classroom
     *
     * @param p1 first profile to compare with
     * @param p2 second profile to compare with
     * @return int of the scare
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        int score = 0;
        // Matching courses
        List<Course> courses = p1.matchingCourses(p2);

        // Loops through each matched course and calculates the age
        for (int i = 0; i < courses.size(); i++) {
            int totalP1 = (currentYear * 4) + convertSessionToInt(this.currentSession);
            int totalP2 = (courses.get(i).getYear() * 4) + convertSessionToInt(courses.get(i).getSession());

            int age = (totalP1 - totalP2) - 1;

            // Determines score based on age
            switch (age) {
                case 0:
                    score += 5;
                case 1:
                    score += 4;
                case 2:
                    score += 3;
                case 3:
                    score += 2;
                case 4:
                    score += 1;
            }
        }
        return score;
    }
}
