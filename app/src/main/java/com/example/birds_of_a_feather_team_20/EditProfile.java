package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {
    String name;
    String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getName();
        getURL();;
//        MyProfile myProfile = new MyProfile(name, photoURL);
//        myProfile.setName(name);
//        myProfile.setPhotoURL(photoURL);

    }
    public void onHomeClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveProfile();
    }

    public void getName(){
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = nameView.getText().toString();
        nameView.setText(name);
    }

    public void getURL(){
        TextView urlView = (TextView)findViewById(R.id.photo_url_textview);
        photoURL = urlView.getText().toString();
        urlView.setText(photoURL);
    }

    public void saveProfile(){
        MyProfile.singleton(getApplicationContext()).setName(name);
        MyProfile.singleton(getApplicationContext()).setPhotoURL(photoURL);
    }
}

