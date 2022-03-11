package com.example.birds_of_a_feather_team_20.wave;

import android.content.Context;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.sorting.ComparatorFactory;
import com.example.birds_of_a_feather_team_20.sorting.ProfileComparator;

public class WaveComparatorFactory extends ComparatorFactory {
    @Override
    public ProfileComparator chooseComp(String sortType, Context context, Profile myProfile) {
        ProfileComparator comp = super.chooseComp(sortType, context, myProfile);
        return new WaveComparator(comp);
    }
}
