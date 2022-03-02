package com.example.birds_of_a_feather_team_20.sorting;

import com.example.birds_of_a_feather_team_20.Profile;

public class MatchScoreTimeWeighted implements ProfileComparator{

    private final String currentSession;
    private final int currentYear;

    public MatchScoreTimeWeighted(String currentSession, int currentYear){

        this.currentSession = currentSession;
        this.currentYear = currentYear;
    }



    @Override
    public int compare(Profile p1, Profile p2) {
        int score = 0;






        return score;
    }
}
