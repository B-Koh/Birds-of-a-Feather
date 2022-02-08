package com.example.birds_of_a_feather_team_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

//        setupNearbyMessage();
    }

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    /*private static final String TAG = "BIRDS OF A FEATHER!";
    private Message mMessage;
    private MessageListener mMessageListener;

    public void setupNearbyMessage() {
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
        mMessage = new Message("Hello World".getBytes());
//        mMessage = new
    }

    @Override
    protected void onStart() { // When is onStart invoked?
        super.onStart();

        Nearby.getMessagesClient(this).publish(mMessage);
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    protected void onStop() { // FIXME: we may want to continue publishing, per the directions
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }*/
}