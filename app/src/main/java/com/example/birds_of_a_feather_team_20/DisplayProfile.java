package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.wave.WavePublisher;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Class for displaying MyProfile and the profile of others. Includes a button to wave.
 */
public class DisplayProfile extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;

    private Profile profile;
    private List<Course> courses;
    private String name;
    private Bitmap profileIm;
    private boolean isMyProfile = false;

    private ImageButton waveButton;

    /**
     * Initialize the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        waveButton = findViewById(R.id.wave_button);

        int index = intent.getIntExtra("index_in_profilesList", 0);
        String profileData = intent.getStringExtra("profile_data");
        profile = Profile.deserialize(profileData);

//        List<Profile> profiles = ProfilesCollection.singleton().getProfiles();

        isMyProfile = profile.getId().equals(MyProfile.singleton(this).getId());
        boolean isInCurrentSession = false;
        for (Profile p : ProfilesCollection.singleton().getProfiles()) {
            if (p.getId().equals(profile.getId())) {
                isInCurrentSession = true;
                break;
            }
        }
        if (isMyProfile || !isInCurrentSession) {
            if (waveButton != null)
                waveButton.setVisibility(View.INVISIBLE);
        }

//        if (index == -1) {
//            profile = MyProfile.singleton(this);
//            isMyProfile = true;
//            if (waveButton != null)
//                waveButton.setVisibility(View.INVISIBLE);
//        }
//        else {
//            profile = profiles.get(index);
//            isMyProfile = false;
//        }

        setName();
        setCourses();
        setImage();

    }

    /**
     * Display the profile's name
     */
    public void setName(){
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = profile.getName();
        nameView.setText(name);
        if (isMyProfile)
            setTitle("My Profile");
        else
            setTitle(Utilities.getFirstName(name) + "'s Profile");

    }

    /**
     * Fetch and display the profile's image. This happens in a background thread, so the main
     * thread is not halted while the image downloads.
     */
    public void setImage(){
        Executors.newSingleThreadExecutor().submit(() -> {
            ImageView imageView = (ImageView) findViewById(R.id.profile_image);
            Bitmap fullPhoto = profile.getPhoto();
            if (fullPhoto == null) {
                Log.e("DisplayProfile", "fullPhoto is NULL!");
                return;
            }
            profileIm = fullPhoto;
//            profileIm = Bitmap.createScaledBitmap(fullPhoto, 450, 450, true);
//            Bitmap.createScaledBitmap()
            runOnUiThread(() -> {
                imageView.setImageBitmap(profileIm);
            });
        });
    }

    /**
     * Initializes the adapter and recycler view that display the courses.
     */
    public void setCourses(){
        courses = profile.getCourses();

        //Set up the recycler view to show our database contents
        coursesRecyclerView = findViewById(R.id.course_view);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesViewAdapter = new CoursesViewAdapter(courses, this, isMyProfile);

        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    /**
     * Call this to close the activity
     * @param view
     */
    public void onBackClicked(View view) {
        finish();
    }

    /**
     * Sends a wave to the student this profile belongs to.
     * @param view
     */
    public void onWaveClicked(View view) {
        WavePublisher.singleton(this).sendWave(profile, this, (success) -> {
            if (success)
                onWaveSuccess(view);
            else
                onWaveFailure(view);
        });
    }

    /**
     * Display the success Toast if message was sent.
     */
    private void onWaveSuccess(View view) {
        Toast.makeText(this, "Wave sent!", Toast.LENGTH_LONG).show();
        ((ImageButton)view).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_waving_hand_filled));
    }

    /**
     * Display error Toast if message failed.
     */
    private void onWaveFailure(View view) {
        Toast.makeText(this, "Please try to wave again.", Toast.LENGTH_SHORT).show();
    }
}
