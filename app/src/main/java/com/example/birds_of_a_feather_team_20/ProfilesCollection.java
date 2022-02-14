package com.example.birds_of_a_feather_team_20;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProfilesCollection {
    private final List<Profile> profiles;
//    private final List<MatchProfilePair> matchPairs;
//    ProfilesViewAdapter adapter;
    private static ProfilesCollection singletonInstance;

    private final Stack<Integer> additions;
    private final Stack<Integer> modifications;

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

    public ProfilesCollection(/*ProfilesViewAdapter adapter*/) {
        profiles = new ArrayList<>();
//        matchPairs = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();
    }

    public void addOrUpdateProfile(Profile profile, int courseMatches) {
        if (profile == null) return;

//        if (courseMatches == 0) {
//            return; // TODO Don't add to list if no matches
//        }

        int index = getProfiles().indexOf(profile);
        if (index == -1) {
            insertNewProfile(profile, getProfiles().size());
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
