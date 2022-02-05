package com.example.birds_of_a_feather_team_20;

import android.graphics.Bitmap;

/**
 * This class is for representing student profiles. It stores the student's name and the URL to the
 * student's profile photo. This will eventually contain a List of the student's courses too.
 */
public class Profile {
    private String name;
    private String photoURL;
    private Bitmap thumbnail; //Compressed image from URL
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

    /**
     *
     *
     */


    /**
     *
     * @return Uncompressed bitmap from URL if loading successful, null otherwise.
     */
    public Bitmap getPhoto(){
        //TODO: Download image from URL, uncompressed.

        return null;
    }

    /**
     *
     * @return If image has been previously loaded, null otherwise.
     */
    public Bitmap getThumbnail(){
        //TODO: Download image from URL and compress
        if(thumbnail != null) return thumbnail;

        return null;
    }
}
