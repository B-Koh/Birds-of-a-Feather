package com.example.birds_of_a_feather_team_20.sorting;

import android.content.Context;

import com.example.birds_of_a_feather_team_20.MyProfile;
import com.example.birds_of_a_feather_team_20.Profile;

public class ComparatorFactory {
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
