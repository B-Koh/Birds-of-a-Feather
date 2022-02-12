package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisplayProfile extends AppCompatActivity {

//    private AppDatabase db;

//    private RecyclerView coursesRecyclerView;
//    private RecyclerView.LayoutManager coursesLayoutManager;
//    private CoursesViewAdapter coursesViewAdapter;

    Profile profile;
    //    Courses courses;
    String name;
    Bitmap profileIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setName();
        setImage();
        setCourses();

    }

    public void setName(){
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = MyProfile.singleton(getApplicationContext()).getName();
        nameView.setText(name);

    }

    public void setImage(){
        ImageView imageView = (ImageView) findViewById(R.id.profile_image);
        imageView.setImageBitmap(profileIm);



    }

    public void setCourses(){
//        db = AppDatabase.singleton(this);

//        coursesRecyclerView = findViewById(R.id.courses_view);
//        coursesLayoutManager = new LinearLayoutManager(this);
//        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

//        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }




}
