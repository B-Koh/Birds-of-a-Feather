package com.example.birds_of_a_feather_team_20;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;

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
        // If url has been updated, reset the thumbnail
        if(this.photoURL == null || this.photoURL.equals(photoURL) == false) {
            this.photoURL = photoURL;
            this.thumbnail = null; //thumbnail must be null for getThumbnail to update it.
            getThumbnail();
        }
        // Otherwise, update just the URL.
        this.photoURL = photoURL;
    }


    /**
     * Retrieves full-sized image from URL as bitmap, if image can be loaded from photoURL. If URL
     * is unset or cannot be retrieved as an image, returns null.
     *
     * Recommended to run this in background.
     *
     * @return Uncompressed bitmap from URL if loading successful, null otherwise.
     */
    public Bitmap getPhoto(){
        Bitmap photo; //Bitmap to return to user

        //Send provided URL to input stream
        try {
            InputStream photoURLstream = new java.net.URL(photoURL).openStream();
            //photo will be null if the URL cannot be decoded to an image.
            photo = BitmapFactory.decodeStream(photoURLstream);

            photoURLstream.close();
            return photo;
        } catch(Exception e){
            //Possible exception if photoURL is not a URL, return null
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a 256 by 256 pixel version of the image provided in the URL, if valid. For non-square
     * images, the method will compress the image to make it square.
     *
     * Recommended to run this in background.
     *
     * @return If image has been previously loaded, null otherwise.
     */
    public Bitmap getThumbnail(){
        // If thumbnail is set (and URL has not been changed since the thumbnail was set), returns
        // existing thumbnail.
        if(thumbnail != null) return thumbnail;

        // Download fullsized image, for compression
        Bitmap fullPhoto = getPhoto();
        // Catch case for unset or invalid URL.
        if(fullPhoto == null) return null;

        //Compress (or extend) image such that result is 256 by 256.
        thumbnail = Bitmap.createScaledBitmap(fullPhoto, 256, 256, true);

        return thumbnail;
    }
}
