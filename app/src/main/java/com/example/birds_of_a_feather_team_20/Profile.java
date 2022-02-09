package com.example.birds_of_a_feather_team_20;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This class is for representing student profiles. It stores the student's name and the URL to the
 * student's profile photo. This will eventually contain a List of the student's courses too.
 */
public class Profile {
    private String id;
    private String name;
    private String photoURL;
    private Bitmap thumbnail; //Compressed image from URL
    // private List<Course> courses; // TODO


    public Profile(String name, String photoURL, String id) {
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
        if(this.photoURL == null || !this.photoURL.equals(photoURL)) {
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
    public Bitmap getPhoto() {
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

    public String serialize() {
        /*JSONObject json = new JSONObject();
        String name = this.getName();
        String url = this.getPhotoURL();
        try {
            json.put("name", name);
            json.put("photo_url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();*/
        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(out);
        try {
            writer.beginObject();
            writer.name("user_id").value(this.getId());
            writer.name("name").value(this.getName()); // NOT SURE IF THIS WILL BE CALLED ON THE CHILD CLASS
            writer.name("photo_url").value(this.getPhotoURL());
            writer.endObject();
            writer.close();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deserialize(String data) {
        // https://developer.android.com/reference/android/util/JsonReader
        StringReader in = new StringReader(data);
        JsonReader reader = new JsonReader(in);
        try {
            // read name and URL
            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                switch (key) {
                    case "user_id":
                        this.setId(reader.nextString());
                        break;
                    case "name":
                        this.setName(reader.nextString());
//                        this.name = reader.nextString();
                        break;
                    case "photo_url":
                        this.setPhotoURL(reader.nextString());
//                        this.photoURL = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            // TODO read courses array
            reader.close();
        } catch (IOException e) {
            // TODO Print toast notification
            e.printStackTrace();
        }
        in.close();
    }
}
