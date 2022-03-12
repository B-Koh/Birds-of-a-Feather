package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class that helps enable/disable toasts.
 */
public class Utilities {

    public static boolean debugToast = false; // instance variable that can turn on/off toasts

    /**
     * Method helps determine whether a toast will appear depending on instance variable
     * @param context - context of where toast will appear
     * @param message - message that toast will contain
     */
    public static void logToast(Context context, String message) {
        Log.d(context.toString(), message);
        if (!debugToast) {
            return;
        }
        toast(context, message);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Scales down (or up) an image, maintaining the aspect ratio. The resulting dimensions will always
     * be greater than or equal to the minWidth and minHeight specified.
     */
    public static Bitmap rescaleBitmap(Bitmap original, int minWidth, int minHeight) {
        // Want the image to fill the minWidth by maxWidth frame.
        // Is the image too tall or too wide?
        float originalRatio = original.getHeight() / (float)original.getWidth();
        float targetRatio = minHeight / (float)minWidth;
        boolean tooTall = originalRatio > targetRatio;

        int resizedWidth, resizedHeight;

        // If too tall then width will be the constrained dimension, otherwise height will be
        if (tooTall) {
            resizedWidth = minWidth;
            resizedHeight = Math.round(minWidth * originalRatio);
        } else {
            resizedWidth = Math.round(minHeight / originalRatio);
            resizedHeight = minHeight;
        }
        Log.d("Resizing", "resizedWidth=" + resizedWidth + " | resizedHeight=" + resizedHeight);

        return Bitmap.createScaledBitmap(original, resizedWidth, resizedHeight, true);
    }

    private static final String[] names = new String[]{
            "Nelson", "Tamika", "Alvin", "Chasity", "Trent", "Jana", "Enrique", "Tracey",
            "Antoinette", "Jami", "Earl", "Gilbert", "Damien", "Janice", "Christa", "Tessa", "Kirk",
            "Yvette", "Elijah", "Howard", "Elisa", "Desmond", "Clarence", "Alfred", "Darnell",
            "Breanna", "Kerry", "Nickolas", "Maureen", "Karina", "Roderick", "Rochelle", "Rhonda",
            "Keisha", "Irene", "Ethan", "Alice", "Allyson"};

    private static final String[] photoURLs = new String[] {
//            "https://upload.wikimedia.org/wikipedia/commons/e/e7/African_daisy_(Osteospermum_sp._'Pink_Whirls').jpg",
            "https://upload.wikimedia.org/wikipedia/commons/e/eb/Apple_park_cupertino_2019.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg",
            "https://upload.wikimedia.org/wikipedia/commons/e/e4/Canaan_Dog.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/4/44/Geisel_Library,_UCSD.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/a/a2/Burning_of_the_uss_philadelphia.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/c/c2/Choctaw_-_Howard_Freeman_Sprague.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/5/59/Soyuz_TMA-13_Edit.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/e/e2/Henry_Highland_Garnet_by_James_U._Stead.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/6/64/Heart_Nebula_(2020-08-11).jpg",
//            "https://upload.wikimedia.org/wikipedia/commons/6/63/Ely_Cathedral_Lady_Chapel,_Cambridgeshire,_UK_-_Diliff.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/e/e2/Khalili_Collections_A_Composite_Imaginary_View_of_Japan.jpg",
//            "https://upload.wikimedia.org/wikipedia/commons/9/90/Zayapa_(Grapsus_grapsus),_Las_Bachas,_isla_Santa_Cruz,_islas_Gal%C3%A1pagos,_Ecuador,_2015-07-23,_DD_30.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/2/2e/Lama_glama_Laguna_Colorada_2.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Nursery_web_spider_(Pisaura_mirabilis)_2.jpg/1024px-Nursery_web_spider_(Pisaura_mirabilis)_2.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Staring_Down_Hurricane_Florence.jpg/1920px-Staring_Down_Hurricane_Florence.jpg"

    };

    public static Profile generateProfile(Context context) {
        Random rand = new Random();

        String name = names[rand.nextInt(names.length)] + " " + names[rand.nextInt(names.length)];
        String url = photoURLs[rand.nextInt(photoURLs.length)];

        Profile profile = new Profile(name, url, String.valueOf(rand.nextInt()));
        profile.setCourses(generateCourses(MyProfile.singleton(context)));
        return profile;

    }
    public static List<Course> generateCourses(Profile myProfile) {
        Random rand = new Random();
        List<Course> myCourses = myProfile.getCourses();
        int numberOfCoursesMax = rand.nextInt(myCourses.size() + 1);
        if (myCourses.size() > 0 && numberOfCoursesMax == 0)
            numberOfCoursesMax = 1;
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < numberOfCoursesMax; i++) {
            Course c = myCourses.get(rand.nextInt(myCourses.size()));
            if (!courses.contains(c))
                courses.add(c);
        }
        return courses;
    }

    public static String getFirstName(String fullName) {
        return fullName.trim().split(" ")[0];
    }

    public static Profile pickRandomProfile() {
        List<Profile> profiles = ProfilesCollection.singleton().getProfiles();
        if (profiles.isEmpty())
            return null;
        return profiles.get(new Random().nextInt(profiles.size()));
    }

    public static String coursesToString(List<Course> courses) {
//        courses.sort();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, coursesSize = courses.size(); i < coursesSize; i++) {
            Course course = courses.get(i);
            boolean isLast = (i == coursesSize - 1);
            builder.append(course.getDepartment()).append(" ").append(course.getCourseNumber());
            if (!isLast) builder.append(", ");
        }
        return builder.toString();
    }
}

