package com.example.birds_of_a_feather_team_20.sorting;

import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.List;

public class MatchScoreSizeWeighted implements ProfileComparator {


    /**
     * compares the matches based on the size of the classroom
     * @param p1 first profile to compare with
     * @param p2 second profile to compare with
     * @return float of the scare multiplied by 100
     */
    @Override
    public int compare(Profile p1, Profile p2) {
        float score = 0;
        List<Course> courses = p1.matchingCourses(p2);

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
        return (int) (score * 100);
    }
}
