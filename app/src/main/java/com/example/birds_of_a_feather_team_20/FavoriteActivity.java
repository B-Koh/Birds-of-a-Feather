package com.example.birds_of_a_feather_team_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This class is used to manage the activity of the Favorites Tab. Handles actions and buttons
 * to different activities within the app.
 */
public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle("My Favorites");
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    public void onLaunchProfileClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void onLaunchDebugClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    public void onLaunchFriendsClicked(View view) {
        finish();
    }

}