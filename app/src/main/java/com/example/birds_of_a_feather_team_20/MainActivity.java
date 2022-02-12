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
    private static final boolean showDebugToast = false;

    private Message profileMessage;
    private MessageListener profileMessageListener;
    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }
    public List<Profile> foundProfiles;
    public static Stack<Integer> additions;
    public static Stack<Integer> modifications;

    RecyclerView basicRecycler;
    RecyclerView.LayoutManager layoutManager;
    ProfilesViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");
        MyProfile.singleton(getApplicationContext());
        initializeNearby();
    }

    @Override
    protected void onStart() {
        Log.i("START", "onStart");

        super.onStart();

        subscribe();
        publish();
    }

    @Override
    protected void onStop() {
        Log.i("STOP", "onStop");

        unsubscribe();
        unpublish();

        super.onStop();
    }

    /**
     * Print a Log message and also display a Toast notification
     * @param str
     */
    private void toastLog(String str) {
        Log.d("MainActivity", str);
        if (!showDebugToast) return;
        runOnUiThread(() -> {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeNearby() {
        foundProfiles = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();

        basicRecycler = findViewById(R.id.profile_list);
        layoutManager = new LinearLayoutManager(this);
        basicRecycler.setLayoutManager(layoutManager);
        adapter = new ProfilesViewAdapter(foundProfiles);
        basicRecycler.setAdapter(adapter);

        profileMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                String msgBody = new String(message.getContent());
                toastLog("Found profile: " + msgBody);
                onFoundProfile(msgBody);
            }

            @Override
            public void onLost(final Message message) {
                String msgBody = new String(message.getContent());
                toastLog("Lost profile: " + msgBody);
            }
        };
    }

    private void onFoundProfile(String profileData) {
        // A profile was received, now process it
        Profile profile = Profile.deserialize(profileData);
        if (profile == null) return;
        addProfile(profile);
        refreshProfileListView();
    }

    protected void addProfile(Profile profile) {
        for (int i = 0, nearbyProfilesSize = foundProfiles.size(); i < nearbyProfilesSize; i++) {
            Profile p = foundProfiles.get(i);
            if (p.getId().equals(profile.getId())) {
                if (!p.getName().equals(profile.getName()) || !p.getPhotoURL().equals(profile.getPhotoURL())) {
                    toastLog("Update existing profile: " + p.getName());
                    modifications.add(i);
                    p.setName(profile.getName()); // update name if id matches
                    p.setPhotoURL(profile.getPhotoURL()); // update url if id matches
                }
                // Don't want to notify the recycler if nothing changed (because it will play an animation)
                return;
            }
        }
        toastLog("Adding to List: " + profile.serialize());
        additions.add(foundProfiles.size());
        foundProfiles.add(profile); // no profile with this id, so add it
    }

    private void refreshProfileListView() {
        setTitle("Find Friends (" + foundProfiles.size() + ")");
        // Refresh on background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            updateThumbnailsBackground();
            runOnUiThread(this::updateList);

            /*updateThumbnailsBackground();
            toastLog("The number of List items is: " + foundProfiles.size());
            runOnUiThread(() -> {
                // notify
                adapter.notifyDataSetChanged();

            });*/

            return null;
        });
    }


    private void subscribe() {
        toastLog("Subscribing to nearby profiles...");
        Nearby.getMessagesClient(this).subscribe(profileMessageListener);
    }
    private void unsubscribe() {
        toastLog("Unsubscribing...");
        Nearby.getMessagesClient(this).unsubscribe(profileMessageListener);
    }
    private void publish() {
        profileMessage = new Message(MyProfile.singleton(this).serialize().getBytes(StandardCharsets.UTF_8));
        toastLog("Publishing my profile...");
        Nearby.getMessagesClient(this).publish(profileMessage).addOnFailureListener(e ->
                toastLog("API Error!"));
    }
    private void unpublish() {
        toastLog("Unpublishing...");
        Nearby.getMessagesClient(this).unpublish(profileMessage);
    }

    //////// END REFACTORING



    private void sendFakeMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        profileMessageListener.onFound(message);
        profileMessageListener.onLost(message);
    }

    public void updateList() {

        ProfilesViewAdapter.update(this);
        while(!modifications.isEmpty()) {
            Integer i = modifications.pop();
            if (i != null)
                adapter.notifyItemChanged(i);
        }
        while(!additions.isEmpty()) {
            Integer i = additions.pop();
            if (i != null)
                adapter.notifyItemInserted(i);
        }
//        adapter.notifyDataSetChanged();
//        adapter = new ProfilesViewAdapter(NearbyManager.nearbyProfiles);
//        basicRecycler.setAdapter(adapter);
    }

    void updateThumbnailsBackground() {
//        for (Profile p : foundProfiles) {
//            if (p != null) {
//                p.getThumbnail();
//            }
//        }

        for(Integer i : modifications) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
        for(Integer i : additions) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
    }

    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }


}