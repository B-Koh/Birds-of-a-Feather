package com.example.birds_of_a_feather_team_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

//import com.google.android.gms.nearby.Nearby;
//import com.google.android.gms.nearby.messages.Message;
//import com.google.android.gms.nearby.messages.MessageListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");
        MyProfile.singleton(getApplicationContext());

    }

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

//    private static final String TAG = "BIRDS OF A FEATHER!";
//    private Message mMessage;
//    private MessageListener mMessageListener;

    /*public void setupNearbyMessage() { // TODO will need to set up again (and maybe unpublish old + publish new) when the user changes their name or photoURL
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };

        // Need to construct a message with all the user's data
        mMessage = new Message(MyProfile.singleton(getApplicationContext()).serialize()
                .getBytes(StandardCharsets.UTF_8));
    }*/

    /* TODO
    Need to make the serialization tests include the id thing.
    Need to add to arraylist when the id is new.
     */

    @Override
    protected void onStart() { // When is onStart invoked?
        super.onStart();
        NearbyManager.startNearby(this);
    }

    @Override
    protected void onStop() {
        NearbyManager.stopNearby(this);
        super.onStop();
    }
}