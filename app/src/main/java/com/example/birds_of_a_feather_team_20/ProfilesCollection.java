package com.example.birds_of_a_feather_team_20;

import java.util.ArrayList;
import java.util.List;

public class ProfilesCollection {
    private final List<Profile> profiles;
    private final List<MatchProfilePair> matchPairs;
//    ProfilesViewAdapter adapter;
    private static ProfilesCollection singletonInstance;

    public static ProfilesCollection singleton() {
        if (singletonInstance == null) {
            singletonInstance = new ProfilesCollection();
        }
        return singletonInstance;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public ProfilesCollection(/*ProfilesViewAdapter adapter*/) {
        profiles = new ArrayList<>();
        matchPairs = new ArrayList<>();
    }

    public void add(int index, Profile profile) {
        profiles.add(index, profile);
//        matchPairs.add(index, new MatchProfilePair(, profile));
    }

    public Profile get(int index) {
        return profiles.get(index);
    }

    public int indexOf(Profile profile) {
        return profiles.indexOf(profile);
    }

    public int size() {
        return profiles.size();
    }

    public void clear() {
        profiles.clear();
    }

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
