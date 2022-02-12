package com.example.birds_of_a_feather_team_20;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;

public class NearbyManager {
    private Message profileMessage;
    private MessageListener profileMessageListener;

    public List<Profile> foundProfiles;
    public static Stack<Integer> additions;
    public static Stack<Integer> modifications;

    RecyclerView basicRecycler;
    RecyclerView.LayoutManager layoutManager;
    ProfilesViewAdapter adapter;

    MainActivity mainActivity;
    public NearbyManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        foundProfiles = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();

        initializeNearby();
    }

    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }

    /**
     * Print a Log message and also display a Toast notification
     * @param str
     */
    private void toastLog(String str) {
        Utilities.logToast(mainActivity, str);
    }

    public void sendFakeMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        profileMessageListener.onFound(message);
        profileMessageListener.onLost(message);
    }

    public void initializeNearby() {


        basicRecycler = mainActivity.findViewById(R.id.profile_list);
        layoutManager = new LinearLayoutManager(mainActivity);
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

    private void addProfile(Profile profile) {
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
        mainActivity.setTitle("Find Friends (" + foundProfiles.size() + ")");
        // Refresh on background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            updateThumbnailsBackground();
            mainActivity.runOnUiThread(this::updateList);

            /*updateThumbnailsBackground();
            toastLog("The number of List items is: " + foundProfiles.size());
            runOnUiThread(() -> {
                // notify
                adapter.notifyDataSetChanged();

            });*/

            return null;
        });
    }


    public void subscribe() {
        toastLog("Subscribing to nearby profiles...");
        Nearby.getMessagesClient(mainActivity).subscribe(profileMessageListener);
    }
    public void unsubscribe() {
        toastLog("Unsubscribing...");
        Nearby.getMessagesClient(mainActivity).unsubscribe(profileMessageListener);
    }
    public void publish() {
        profileMessage = new Message(MyProfile.singleton(mainActivity).serialize().getBytes(StandardCharsets.UTF_8));
        toastLog("Publishing my profile...");
        Nearby.getMessagesClient(mainActivity).publish(profileMessage).addOnFailureListener(e ->
                toastLog("API Error!"));
    }
    public void unpublish() {
        toastLog("Unpublishing...");
        Nearby.getMessagesClient(mainActivity).unpublish(profileMessage);
    }



    private void updateList() {

        ProfilesViewAdapter.update(mainActivity);
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

    private void updateThumbnailsBackground() {
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
}
