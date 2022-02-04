package com.example.birds_of_a_feather_team_20;

/**
 * This class is for representing student profiles. It stores the student's name and the URL to the
 * student's profile photo. This will eventually contain a List of the student's courses too.
 */
public class Profile {
    private String name;
    private String photoURL;
    // private Image photo; // TODO
    // private List<Course> courses; // TODO

    public Profile(String name, String photoURL) {
        this.name = name;
        this.photoURL = photoURL;
    }

    public String getName() {
        return name;
    }
    public String getPhotoURL() {
        return photoURL;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
