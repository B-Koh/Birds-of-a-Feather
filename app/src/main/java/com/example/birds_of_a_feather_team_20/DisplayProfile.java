package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;

public class DisplayProfile extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;

    private Profile profile;
    private List<Course> courses;
    private String name;
    private Bitmap profileIm;
    private boolean isMyProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();

        int index = intent.getIntExtra("index_in_profilesList", 0);

        List<Profile> profiles = ProfilesCollection.singleton().getProfiles();
        if (index == -1) {
            profile = MyProfile.singleton(this);
            isMyProfile = true;
        } else {
            profile = profiles.get(index);
            isMyProfile = false;
        }
        setName();
        setCourses();
        setImage();

    }

    public void setName(){
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = profile.getName();
        nameView.setText(name);
        if (isMyProfile)
            setTitle("My Profile");
        else
            setTitle(Utilities.getFirstName(name) + "'s Profile");

    }

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

    public void setCourses(){
        courses = profile.getCourses();

        //Set up the recycler view to show our database contents
        coursesRecyclerView = findViewById(R.id.course_view);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesViewAdapter = new CoursesViewAdapter(courses, this, isMyProfile);

        coursesRecyclerView.setAdapter(coursesViewAdapter);

    }

    public void onBackClicked(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }


}
