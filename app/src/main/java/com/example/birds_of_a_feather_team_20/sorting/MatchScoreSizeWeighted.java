package com.example.birds_of_a_feather_team_20.sorting;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.List;

public class MatchScoreSizeWeighted {

    private final Profile myProfile;

    public MatchScoreSizeWeighted(Profile myProfile){
        this.myProfile = myProfile;
    }

    public float calculateSizeWeight(Profile otherProfile){

        float score = 0;
        List courses = myProfile.matchingCourses(otherProfile);

        for(int i = 0; i < courses.size(); i++){
            int classSize = courses.get(i).getClassSize();
            if(classSize < 40){
                score += 1;
            }
            else if(classSize >= 40 && classSize < 75){
                score += 0.33;
            }
            else if(classSize >= 75 && classSize < 150){
                score += 0.18;
            }
            else if(classSize >= 150 && classSize < 250){
                score += 0.10;
            }
            else if(classSize >= 250 && classSize < 400){
                score += 0.06;
            }
            else{
                score += 0.03;
            }
        }
        return score;
    }
}
