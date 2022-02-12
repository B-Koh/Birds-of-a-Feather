package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class is an extension of Profile to represent the student using the app. For all other
 * students, use Profile. Call methods setName() and setPhotoURL() to change the profile data
 * in the shared preferences. This class can be accessed as a Singleton from any activity using
 * MyProfile.singleton(getApplicationContext()).
 */
public class MyProfile extends Profile {
    private static final String NAME_KEY = "name";
    private static final String URL_KEY = "photo_url"; // Note: If you change these, edit the tests.

    private static SharedPreferences preferences;
    private static MyProfile singletonInstance;

    public MyProfile(String name, String photoURL) {
        super(name, photoURL);
    }

    /**
     * Access this class as a Singleton.
     * @param context Recommended to use getApplicationContext() for this.
     * @return reference to the local student's profile
     */
    public static MyProfile singleton(Context context) {
//        if (preferences == null) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        }
        if (singletonInstance == null) {
            singletonInstance = loadProfile(context);
        }
        return singletonInstance;
    }

    /**
     * Helper method for loading the local student's profile from shared preferences.
     */
    private static MyProfile loadProfile(Context context) {
        String name = preferences.getString(NAME_KEY,null);
        String url = preferences.getString(URL_KEY,null);

        return new MyProfile(name, url);
    }

    /**
     * Updates local student's name and saves the change to shared prefs.
     * @param name New name
     */
    public void setName(String name, SharedPreferences prefs) {
        super.setName(name);
//        SharedPreferences.Editor editor = preferences.edit();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAME_KEY, name);
        editor.apply();
    }
    /**
     * Updates local student's name and saves the change to shared prefs.
     * @param name New name
     */
    public void setName(String name) {
        super.setName(name);
//        SharedPreferences.Editor editor = preferences.edit();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME_KEY, name);
        editor.apply();
    }

    /**
     * Updates local student's photo URL and saves the change to shared prefs.
     * @param photoURL URL to the new photo
     */
    public void setPhotoURL(String photoURL) {
        super.setPhotoURL(photoURL);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(URL_KEY, photoURL);
        editor.apply();
    }
}
