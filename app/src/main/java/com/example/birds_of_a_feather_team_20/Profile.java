package com.example.birds_of_a_feather_team_20;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<Course> courses;

    private boolean isFavorite = false;
    private boolean waved = false;
    private int sessionId = -1;


    public Profile(String name, String photoURL, String id) {
//        Log.e("New PROFILE", name + " | " + photoURL);
        this.name = (name != null) ? name : "";
        this.photoURL = (photoURL != null) ? photoURL : "";
        this.id = (id != null) ? id : "";
        courses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return (name != null) ? name : "";
    }
    public List<Course> getCourses(){ return this.courses;}
    public void setCourses(List<Course> courses) {
        if (courses == null)
            return;
        this.courses = courses;
    }
    public String getPhotoURL() {
        return (photoURL != null) ? photoURL : "";
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
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

    public static HashMap<String, Bitmap> imageCache;
    public static HashMap<String, Bitmap> getImageCache() {
        if (imageCache == null)
            imageCache = new HashMap<String, Bitmap>();
        return imageCache;
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
     * Count the number of courses that match between this profile and otherProfile
     * @param otherProfile profile to compare courses to
     * @return number of matching courses
     */
    public int countMatchingCourses(Profile otherProfile){
        //int to return the num of matching courses
        int numMatchCourse = 0;

        for(int i = 0; i < getCourses().size(); i++){
            for(int j = 0; j < otherProfile.getCourses().size(); j++){

                //check if courses are equal
                if(getCourses().get(i).equals(otherProfile.getCourses().get(j))){
                    numMatchCourse++;
                }
            }
        }
        return numMatchCourse;
    }

    /**
     * get a list of matching courses
     * @param otherProfile profile to compare courses to
     * @return list of matching courses
     */
    public List<Course> matchingCourses(Profile otherProfile){

        //arraylist to return the list of matching courses
        List<Course> matchCourseList = new ArrayList<>();

        for(int i = 0; i < getCourses().size(); i++){
            for(int j = 0; j < otherProfile.getCourses().size(); j++){

                //check if courses are equal
                if(getCourses().get(i).equals(otherProfile.getCourses().get(j))){
                    matchCourseList.add(getCourses().get(i));
                }
            }
        }
        return matchCourseList;
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
            InputStream photoURLstream = new URL(photoURL).openStream();
            //photo will be null if the URL cannot be decoded to an image.
            photo = BitmapFactory.decodeStream(photoURLstream);

            photoURLstream.close();
            Log.i("Download Photo - Succeed", "Downloaded " + getPhotoURL());
            return photo;
        } catch (MalformedURLException e) {
            Log.e("Image", "Image URL is invalid.");
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

    public void fetchThumbnail() {
        // If thumbnail is set (and URL has not been changed since the thumbnail was set), returns
        // existing thumbnail.
        String thisURL = getPhotoURL();
        if(thumbnail != null && thisURL.equals(lastDownloadedURL)) {
            Log.e("Thumbnail", "The thumbnail didn't need to be fetched again.");
            return;
        }
        if (getImageCache().containsKey(thisURL)) {
            thumbnail = getImageCache().get(thisURL);
            if (thumbnail != null)
                return;
        }

        // Download fullsized image, for compression
        Bitmap fullPhoto = getPhoto();
        // Catch case for unset or invalid URL.
        if(fullPhoto == null) {
            Log.e("Thumbnail", "Downloaded a null image!");
            return;
        }

        //Compress (or extend) image such that result is 256 by 256.
//        thumbnail = Bitmap.createScaledBitmap(fullPhoto, 256, 256, true);
        thumbnail = Utilities.rescaleBitmap(fullPhoto, 256, 256);
        lastDownloadedURL = thisURL;

        getImageCache().put(thisURL, thumbnail);
    }

    public Bitmap getPrefetchedThumbnail() {
        return thumbnail;
    }

    /**
     * Represent the Profile as a String using JSON
     * https://developer.android.com/reference/android/util/JsonWriter
     * https://www.javadoc.io/doc/com.google.code.gson/gson/2.6.2/com/google/gson/stream/JsonWriter.html
     */
    public String serialize() {
        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(out);
        try {
            writer.beginObject();
            writer.name("user_id").value(this.getId());
            writer.name("name").value(this.getName());
            writer.name("photo_url").value(this.getPhotoURL());
            writer.name("course_data").value(serializeCourses(this.getCourses()));
            //writeCourses(writer);
            writer.endObject();
            writer.close();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String serializeCourses(List<Course> courses) throws IOException {
        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(out);
        writeCourses(writer, courses);
        writer.close();
        return out.toString();
    }

    private static void writeCourses(JsonWriter writer, List<Course> courses) throws IOException {
//        writer.
        writer.beginArray();
        for(Course course : courses) {
            course.writeCourse(writer);
            // writeCourse(writer, course);
        }
        writer.endArray();
    }

//    private static void writeCourse(JsonWriter writer, Course course) throws IOException {
//        writer.beginObject();
//        writer.name("course_data").value(course.serialize());
//        writer.endObject();
//    }

    /**
     * Convert the String to a Profile using JSON
     * https://developer.android.com/reference/android/util/JsonReader
     */
    public static Profile deserialize(String data) {
        if (data == null) return null;
        StringReader in = new StringReader(data);
        JsonReader reader = new JsonReader(in);
        String id = "";
        String name = "";
        String photoURL = "";
        String coursesData = "";
        Profile profile = null;
        try {
            // read name and URL
            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    continue;
                }
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
                    case "course_data":
                        coursesData = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            profile = new Profile(name, photoURL, id);
            profile.setCourses(deserializeCourses(coursesData));

            //readCourses1(profile, coursesData);
            reader.endObject();
            reader.close();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        in.close();

        return profile;
    }

    private static List<Course> deserializeCourses(String data) throws IOException {
        if (data == null || data.equals("")) return null;

        StringReader in = new StringReader(data);
        JsonReader reader = new JsonReader(in);
        List<Course> courses = readCourses(reader, data);
        reader.close();


        return courses; // FIXME
    }

    private static List<Course> readCourses(JsonReader reader, String data) throws IOException {
        List<Course> courses = new ArrayList<Course>();
        reader.beginArray();
        while(reader.hasNext()) {
            courses.add(Course.readCourse(reader));
        }
        reader.endArray();
        return courses;
    }

//    private static void readCourses1(Profile profile, String coursesData) throws IOException {
//        if (coursesData == null || coursesData.equals("")) return;
//
////        profile.addCourse();
//        StringReader in = new StringReader(coursesData);
//        JsonReader reader = new JsonReader(in);
////        String id = "";
////        String name = "";
////        String photoURL = "";
////        String coursesData = "";
////        Profile profile = null;
//        reader.beginArray();
//        while (reader.hasNext()) {
////            profile.addCourse(readCourse(reader));
//        }
//        reader.endArray();
//        reader.close();
////        try {
////             read name and URL
////            reader.beginObject();
////            while(reader.hasNext()) {
////                String key = reader.nextName();
//    }

//    private static Course readCourse(JsonReader reader) throws IOException {
//        Course course = null;
//        reader.beginObject();
////        while(reader.hasNext()) {
//            if (reader.nextName().equals("course_data")) {
//                course = Course.deserialize(reader.nextString());
//            } else {
//                reader.skipValue();
//            }
////        }
//        reader.endObject();
//        return course;
//    }

    // TODO match this up with what brandon added
    public void addCourse(Course course) {
        if (!getCourses().contains(course))
            getCourses().add(course);
    }

    public boolean isValid() {
        // OK for the photoURL to be ""
        return getName() != null && getId() != null && getPhotoURL() != null &&
                !getName().trim().equals("") && !getId().trim().equals("");
    }




    /**
     * Checks if profile is a favorited or not
     * @return true if profile is favorite / false if profile is not favorite
     */
    public boolean getIsFavorite() {
        return this.isFavorite;
    }

    /**
     * Sets a profile to favorited
     */
    public void setFavorite() {
        this.isFavorite = true;
    }

    /**
     * Sets a profile to not favorited
     */
    public void unFavorite() {
        this.isFavorite = false;
    }

    public void setWavedAtMe(boolean waved) {
        this.waved = waved;
    }

    public boolean getWavedAtMe() {
        return waved;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }
}
