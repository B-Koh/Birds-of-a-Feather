package com.example.birds_of_a_feather_team_20.sorting;

import android.content.Context;

import com.example.birds_of_a_feather_team_20.Profile;

/**
 * This class is for selecting which comparator to use. It will handle the creation of
 * ProfileComparator objects.
 */
public class ComparatorFactory {
    /**
     * Call this method to get the appropriate ProfileComparator for sorting profiles
     * @param sortType Indicates the comparator
     * @param context context
     * @param myProfile The local profile; the comparators rely on this
     * @return The appropriate ProfileComparator
     */
    public ProfileComparator chooseComp(String sortType, Context context, Profile myProfile) {
        ProfileComparator comp;
        switch (sortType) {
            case TimeWeightComparator.LABEL:
                comp = new TimeWeightComparator("WI", 2022, myProfile);
                break;
            case SizeWeightComparator.LABEL:
                comp = new SizeWeightComparator(myProfile);
                break;
            case MatchComparator.LABEL:
            default:
                comp = new MatchComparator(myProfile);
                break;
        }
        return comp;
    }
}
