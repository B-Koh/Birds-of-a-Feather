package com.example.birds_of_a_feather_team_20.sorting;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.Comparator;

/**
 * Implement this interface to specify new kinds of sorting
 */
public interface ProfileComparator extends Comparator<Profile> {
    int compare(Profile p1, Profile p2);
}
