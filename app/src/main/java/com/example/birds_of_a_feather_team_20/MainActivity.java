package com.example.birds_of_a_feather_team_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private NearbyManager nearbyManager;

    public NearbyManager getNearbyManager() {
        return nearbyManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");

        MyProfile.singleton(getApplicationContext()); // This line is probably unnecessary

        nearbyManager = new NearbyManager(this);
    }

    @Override
    protected void onStart() {
        Log.i("START", "MainActivity.onStart");

        super.onStart();

        nearbyManager.subscribe();
        nearbyManager.publish();
    }

    @Override
    protected void onStop() {
        Log.i("STOP", "MainActivity.onStop");

        nearbyManager.unsubscribe();
        nearbyManager.unpublish();

        super.onStop();
    }

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }
}