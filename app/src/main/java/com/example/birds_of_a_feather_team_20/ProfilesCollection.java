package com.example.birds_of_a_feather_team_20;

import android.util.Log;
import android.util.Pair;

import com.example.birds_of_a_feather_team_20.sorting.ProfileComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ProfilesCollection {
    private final List<Profile> profiles;
//    private final List<MatchProfilePair> matchPairs;
//    ProfilesViewAdapter adapter;
    private static ProfilesCollection singletonInstance;

    private final Stack<Integer> additions;
    private final Stack<Integer> modifications;
    private final Stack<Pair<Integer, Integer>> movements;
    private final HashMap<Profile, Integer> oldPositions;

    private ProfileComparator comparator;

    public static ProfilesCollection singleton() {
        if (singletonInstance == null) {
            singletonInstance = new ProfilesCollection();
        }
        return singletonInstance;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }
    public Stack<Integer> getAdditions() {
        return additions;
    }
    public Stack<Integer> getModifications() {
        return modifications;
    }
    public Stack<Pair<Integer, Integer>> getMovements() { return movements; }

    public ProfilesCollection(/*ProfilesViewAdapter adapter*/) {
        profiles = new ArrayList<>();
//        matchPairs = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();
        movements = new Stack<>();
        oldPositions = new HashMap<>();
    }

    public void addOrUpdateProfile(Profile profile, int courseMatches) {
        if (profile == null) return;

        if (courseMatches == 0) {
            return; // Don't add to list if no matches
        }

        int index = getProfiles().indexOf(profile);
        if (index == -1) {
            insertNewProfile(profile, getProfiles().size());
            applySort();
        }
        else {
            updateExistingProfile(profile, index);
        }
    }


    private void updateExistingProfile(Profile newProfile, int index) {
//        Utilities.logToast(activity, "Update existing profile: " + newProfile.getName());

        Profile existing = getProfiles().get(index);
        if(newProfile == null || existing == null) {
            return; // Note: Making your profile null will not remove it from others' lists
        }
        if (existing.strongEquals(newProfile)) return; // Don't update if they already match (would unnecessarily update the list view)
        existing.updateProfile(newProfile);
        getModifications().add(index);
    }
    private void insertNewProfile(Profile profile, int index) {
//        Utilities.logToast(activity, "Adding to List: " + profile.serialize());
        getAdditions().add(index);
        getProfiles().add(index, profile); // no profile with this id, so add it
        // Note, not sure if it is necessary to tell the adapter that all the other items moved down
    }

    public void changeSort(ProfileComparator comparator) {
        this.comparator = comparator;
        applySort();
    }
    private void applySort() {
        recordMovementsStart();
        getProfiles().sort(comparator);
        recordMovementsStop();
        Log.d("Sorted Profiles", "-------");
        for(Profile profile : getProfiles()) {
            Log.d("Sorted Profile", profile.getName());
        }
        /// RECORD MOVEMENTS
    }

    private void recordMovementsStart() {
        oldPositions.clear();
        List<Profile> profileList = getProfiles();
        for (int i = 0; i < profileList.size(); i++) {
            Profile p = profileList.get(i);
            oldPositions.put(p, i);
        }
    }
    private void recordMovementsStop() {
        List<Profile> profileList = getProfiles();
        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
//            oldPositions.put(p, i);
            int oldPos = oldPositions.get(profile);

            if (i != oldPos) {
                getMovements().add(new Pair(oldPos, i));
            }
        }
    }

    // Not implemented yet. Will need to store the number of matches that each Profile has.
    public static class MatchProfilePair implements Comparable<MatchProfilePair> {
        int matchesCount;
        Profile profile;

        public MatchProfilePair(int matches, Profile p) {
            this.matchesCount = matches;
            this.profile = p;
        }

        @Override
        public int compareTo(MatchProfilePair m) {
            return this.matchesCount - m.matchesCount;
        }
    }
}
