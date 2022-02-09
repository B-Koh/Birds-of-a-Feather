package com.example.birds_of_a_feather_team_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");
        MyProfile.singleton(getApplicationContext());

        ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();

        Future<Void> future = backgroundThreadExecutor.submit(() -> {

            basicRecycler = findViewById(R.id.profile_list);
            layoutManager = new LinearLayoutManager(this);
            basicRecycler.setLayoutManager(layoutManager);
            adapter = new ProfilesViewAdapter(NearbyManager.nearbyProfiles);
            basicRecycler.setAdapter(adapter);

            while (true) {
                Thread.sleep(3000);
                sendMessage("{\"user_id\":\"fakeid\",\"name\":\"John F. Kennedy\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}");
//                updateList();
                runOnUiThread(this::updateList);
//                runOnUiThread(this::updateList);
                Thread.sleep(3000);
                sendMessage("{\"user_id\":\"fakeid1\",\"name\":\"Barack Obama\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg\"}");
//                updateList();
                runOnUiThread(this::updateList);
                Thread.sleep(3000);
                sendMessage("{\"user_id\":\"fakeid2\",\"name\":\"Richard Nixon\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/2/2c/Richard_Nixon_presidential_portrait_(1).jpg\"}");
//                updateList();
                runOnUiThread(this::updateList);
                Thread.sleep(3000);
                sendMessage("{\"user_id\":\"fakeid1\",\"name\":\"Barack H. Obama\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg\"}");
//                updateList();
                runOnUiThread(this::updateList);
            }
//            return null;
        });
    }

    private void sendMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        NearbyManager.getMessageListener().onFound(message);
        NearbyManager.getMessageListener().onLost(message);
    }

    private void updateList() {
//        basicRecycler = findViewById(R.id.profile_list);
//        layoutManager = new LinearLayoutManager(this);
//        basicRecycler.setLayoutManager(layoutManager);

//        adapter = new ProfilesViewAdapter(NearbyManager.nearbyProfiles);
        basicRecycler.setAdapter(adapter);
    }
    RecyclerView basicRecycler;
    RecyclerView.LayoutManager layoutManager;

    ProfilesViewAdapter adapter;

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() { // When is onStart invoked?
        super.onStart();
        NearbyManager.startNearby(this);

        // FIXME - JUST FOR TESTING
//        sendMessage("{\"user_id\":\"fakeid\",\"name\":\"John F. Kennedy\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/John_F._Kennedy,_White_House_color_photo_portrait.jpg\"}");
//        sendMessage("{\"user_id\":\"fakeid1\",\"name\":\"Barack Obama\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg\"}");
//        sendMessage("{\"user_id\":\"fakeid2\",\"name\":\"Richard Nixon\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/2/2c/Richard_Nixon_presidential_portrait_(1).jpg\"}");
//        sendMessage("{\"user_id\":\"fakeid1\",\"name\":\"Barack H. Obama\",\"photo_url\":\"https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg\"}");


    }

    @Override
    protected void onStop() {
        NearbyManager.stopNearby(this);
        super.onStop();
    }
}