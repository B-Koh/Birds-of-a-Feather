package com.example.birds_of_a_feather_team_20;

import android.content.Context;
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
        setTitle("Edit Profile");
        getNameAndURL();

    }

    public void onCoursesClicked(View view) {
//        Intent intent = new Intent(this, EditCourses.class);
//        startActivity(intent);
        saveProfile();
        goToEditCourses();
//        finish();
    }

    @Override
    public void onBackPressed() {
        saveProfile();
        super.onBackPressed();
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


    public void goToEditCourses() {
//        finish();
        Intent intent = new Intent(this, EditCourses.class);
        startActivity(intent);
    }
    public void saveProfile() {
        TextView nameView = (TextView)findViewById(R.id.name_textview);
        name = nameView.getText().toString();
        MyProfile.singleton(getApplicationContext()).setName(name);

        TextView urlView = (TextView)findViewById(R.id.photo_url_textview);
        photoURL = urlView.getText().toString();
        MyProfile.singleton(getApplicationContext()).setPhotoURL(photoURL);
    }

    public void onClickFinish(View view) {
        saveProfile();
        finish();
    }
    public void onClickViewMyProfile(View view) {
        saveProfile();
//        finish();
        Context context = view.getContext();
        Intent intent = new Intent(context, DisplayProfile.class);
        intent.putExtra("index_in_profilesList", -1);
        context.startActivity(intent);
    }

    public void onLaunchDebugClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    public void onLaunchFavoritesClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void onLaunchFriendsClicked(View view) {
        onClickFinish(view);
    }

    public void onViewSessionsClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, ViewSessionsActivity.class);
        startActivity(intent);
    }
}