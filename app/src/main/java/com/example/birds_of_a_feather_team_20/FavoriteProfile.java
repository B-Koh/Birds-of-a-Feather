//package com.example.birds_of_a_feather_team_20;
//
///**
// * This class handles the favorite functions of a profile. It can set a profile to favorited,
// * unfavorite a class, and check if a class is favorited.
// */
//public class FavoriteProfile {
//
//    private Profile profile;
//
//    /**
//     * Constructor
//     * @param profile - profile that will use favorite functions
//     */
//    public void FavoriteProfile(Profile profile) {
//        this.profile = profile;
//    }
//
//    /**
//     * Checks if profile is a favorited or not
//     * @return true if profile is favorite / false if profile is not favorite
//     */
//    public boolean isFavorite() {
//        return this.profile.isFavorite;
//    }
//
//    /**
//     * Sets a profile to favorited
//     */
//    public void setFavorite() {
//        if (this.profile.isFavorite) {
//            return;
//        }
//        this.profile.isFavorite = true;
//    }
//
//    /**
//     * Sets a profile to not favorited
//     */
//    public void unFavorite() {
//        if (!this.profile.isFavorite) {
//            return;
//        }
//        this.profile.isFavorite = false;
//    }
//
//}
