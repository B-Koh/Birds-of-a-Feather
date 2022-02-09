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

        getNameAndURL();;

    }

    public void onHomeClicked(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        saveProfile();
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void getNameAndURL() {
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = MyProfile.singleton(getApplicationContext()).getName();
        nameView.setText(name);


        TextView urlView = (TextView)findViewById(R.id.photo_url_textview);
        photoURL = MyProfile.singleton(getApplicationContext()).getPhotoURL();
        urlView.setText(photoURL);
    }


    public void saveProfile() {
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = nameView.getText().toString();
        MyProfile.singleton(getApplicationContext()).setName(name);

        TextView urlView = (TextView)findViewById(R.id.photo_url_textview);
        photoURL = urlView.getText().toString();
        MyProfile.singleton(getApplicationContext()).setPhotoURL(photoURL);
        //getApplicationContext().update
    }
}