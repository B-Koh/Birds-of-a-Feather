package com.example.birds_of_a_feather_team_20;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

/**
 * This class is for representing student profiles. It stores the student's name and the URL to the
 * student's profile photo. This will eventually contain a List of the student's courses too.
 */
public class Profile {
    private String id;
    private String name;
    private String photoURL;

    private Bitmap thumbnail; //Compressed image from URL
    private String lastDownloadedURL;
    // private List<Course> courses; // TODO


    public Profile(String name, String photoURL, String id) {
//        Log.e("New PROFILE", name + " | " + photoURL);
        this.name = name;
        this.photoURL = photoURL;
        this.id = id;
    }

    public String getId() {
        return id;
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

    private void setId(String id) {
        this.id = id;
    }
    public void setPhotoURL(String photoURL) {
        // If url has been updated, reset the thumbnail
//        if(this.photoURL == null || !this.photoURL.equals(photoURL)) {
//            this.photoURL = photoURL;
//            this.thumbnail = null; //thumbnail must be null for getThumbnail to update it.
//            getThumbnail();
//        }
        // Otherwise, update just the URL.
        this.photoURL = photoURL;
    }

    /**
     * Two profiles are equal if they have the same id. Everything else is ignored.
     */
    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null) return false;
        if (!(other instanceof Profile)) return false;
        Profile casted = (Profile)other;
        return Objects.equals(casted.getId(), this.getId());
    }

    /**
     * Two profiles are strongly equal if all their data match.
     */
    public boolean strongEquals(Profile other) {
        return this.equals(other) && Objects.equals(other.getName(), this.getName())
                && Objects.equals(other.getPhotoURL(), this.getPhotoURL());
        // TODO also check courses?
    }

    /**
     * Copy data from the newProfile to this profile.
     */
    public void updateProfile(Profile newProfile) {
        this.setId(newProfile.getId());
        this.setName(newProfile.getName());
        this.setPhotoURL(newProfile.getPhotoURL());
        // TODO Copy courses as well
    }



    /**
     * Retrieves full-sized image from URL as bitmap, if image can be loaded from photoURL. If URL
     * is unset or cannot be retrieved as an image, returns null.
     *
     * Recommended to run this in background.
     *
     * @return Uncompressed bitmap from URL if loading successful, null otherwise.
     */
    public Bitmap getPhoto() {
        Bitmap photo; //Bitmap to return to user
        //Send provided URL to input stream
        try {
            InputStream photoURLstream = new java.net.URL(photoURL).openStream();
            //photo will be null if the URL cannot be decoded to an image.
            photo = BitmapFactory.decodeStream(photoURLstream);

            photoURLstream.close();
            Log.i("Download Photo - Succeed", "Downloaded " + getPhotoURL());
            return photo;
        } catch(Exception e){
            //Possible exception if photoURL is not a URL, return null
            Log.e("Download Photo - Fail", "Couldn't download " + getPhotoURL());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a 256 by 256 pixel version of the image provided in the URL, if valid. For non-square
     * images, the method will compress the image to make it square.
     *
     * Recommended to run this in background.
     * TODO May want to make a separate fetchThumbnail to download, so it doesn't run on UI thread
     *
     * @return If image has been previously loaded, null otherwise.
     */
    public Bitmap getThumbnail(){
        // If thumbnail is set (and URL has not been changed since the thumbnail was set), returns
        // existing thumbnail.
        String thisURL = getPhotoURL();
        if(thumbnail != null && thisURL.equals(lastDownloadedURL)) return thumbnail;

        // Download fullsized image, for compression
        Bitmap fullPhoto = getPhoto();
        // Catch case for unset or invalid URL.
        if(fullPhoto == null) return null;

        //Compress (or extend) image such that result is 256 by 256.
        thumbnail = Bitmap.createScaledBitmap(fullPhoto, 256, 256, true);
        lastDownloadedURL = thisURL;
        return thumbnail;
    }

    /**
     * Represent the Profile as a String using JSON
     * https://developer.android.com/reference/android/util/JsonWriter
     */
    public String serialize() {
        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(out);
        try {
            writer.beginObject();
            writer.name("user_id").value(this.getId());
            writer.name("name").value(this.getName());
            writer.name("photo_url").value(this.getPhotoURL());
            writer.endObject();
            writer.close();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert the String to a Profile using JSON
     * https://developer.android.com/reference/android/util/JsonReader
     */
    public static Profile deserialize(String data) {
        if (data == null) return null;
        StringReader in = new StringReader(data);
        JsonReader reader = new JsonReader(in);
        String id = null;
        String name = null;
        String photoURL = null;
        Profile profile = null;
        try {
            // read name and URL
            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                switch (key) {
                    case "user_id":
                        id = reader.nextString();
                        break;
                    case "name":
                        name = reader.nextString();
                        break;
                    case "photo_url":
                        photoURL = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            // TODO read courses array
            reader.close();
            profile = new Profile(name, photoURL, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        in.close();
        return profile;
    }
}
