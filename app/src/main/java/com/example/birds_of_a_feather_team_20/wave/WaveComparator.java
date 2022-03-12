package com.example.birds_of_a_feather_team_20.wave;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.sorting.ProfileComparator;

/**
 * This comparator wraps around another ProfileComparator (e.g. the SizeWeightComparator) to put
 * profiles that have waved at the top, but otherwise sorts according to the ProfileComparator
 * it wraps around
 */
public class WaveComparator implements ProfileComparator {

    private ProfileComparator comparator;

    /**
     *
     * @param mainComparator comparator to wrap around
     */
    public WaveComparator(ProfileComparator mainComparator) {
        comparator = mainComparator;
    }

    public void setComparator(ProfileComparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Sorts profiles that have waved to the beginning of the list by returning a negative
     * value from this method if only p1 is waved or positive if only p2 is waved.
     * Otherwise sorts according to the comparator
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        if (p1.getWavedAtMe() && !p2.getWavedAtMe()) return -1;
        if (p2.getWavedAtMe() && !p1.getWavedAtMe()) return 1;
        return comparator.compare(p1, p2);
    }
}
