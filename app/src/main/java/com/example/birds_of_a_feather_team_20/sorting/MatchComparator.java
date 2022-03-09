package com.example.birds_of_a_feather_team_20.sorting;

import android.util.Log;

import com.example.birds_of_a_feather_team_20.Profile;

/**
 * Compares by number of courses that each profile has in common with my profile.
 */
public class MatchComparator implements ProfileComparator {
    // Need a reference to my profile, better to not use singleton here so it will be passed into
    // the constructor.
    private final Profile myProfile;

    public MatchComparator(Profile myProfile) {
        super();
        this.myProfile = myProfile;
    }

    /**
     * Result should be less than zero if p1 has more matches, greater than 0 if p2 has more, and
     * equal to 0 if p1 and p2 have an equal number of matches
     * @return
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        // Sort more matches first, so when p1 > p2, compare should be negative
        int result = p2.countMatchingCourses(myProfile) - p1.countMatchingCourses(myProfile);
//        Log.d("Sorted(MC)", p1.getName() + " vs " + p2.getName() + " -> " + result);
        return result;
    }
}
