package com.example.birds_of_a_feather_team_20;

import android.app.Activity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;

public class NearbyManager {

    public List<Profile> foundProfiles;
    public Stack<Integer> additions;
    public Stack<Integer> modifications;

//    RecyclerView basicRecycler;
//    RecyclerView.LayoutManager layoutManager;
//    ProfilesViewAdapter adapter;

    Activity activity;
    private NearbyProfilesListView profilesListView;

    private Message profileMessage;
    private MessageListener profileMessageListener;

    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }

    public NearbyManager(Activity activity) {
        this.activity = activity;

        foundProfiles = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();

        profilesListView = new NearbyProfilesListView(activity, foundProfiles);

        profileMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                String msgBody = new String(message.getContent());
                Utilities.logToast(activity, "Found profile: " + msgBody);
                onFoundProfile(msgBody);
            }

            @Override
            public void onLost(final Message message) {
                String msgBody = new String(message.getContent());
                Utilities.logToast(activity, "Lost profile: " + msgBody);
            }
        };
    }


    public void sendFakeMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        profileMessageListener.onFound(message);
        profileMessageListener.onLost(message);
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
                    Utilities.logToast(activity, "Update existing profile: " + p.getName());
                    modifications.add(i);
                    p.setName(profile.getName()); // update name if id matches
                    p.setPhotoURL(profile.getPhotoURL()); // update url if id matches
                }
                // Don't want to notify the recycler if nothing changed (because it will play an animation)
                return;
            }
        }
        Utilities.logToast(activity, "Adding to List: " + profile.serialize());
        additions.add(foundProfiles.size());
        foundProfiles.add(profile); // no profile with this id, so add it
    }

    private void refreshProfileListView() {
        activity.setTitle("Find Friends (" + foundProfiles.size() + ")");
        // Refresh on background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            profilesListView.updateThumbnailsBackground(modifications, additions);
            activity.runOnUiThread(() -> {
                profilesListView.updateList(modifications, additions);
            });
            return null;
        });
    }

    public void subscribe() {
        Utilities.logToast(activity, "Subscribing to nearby profiles...");
        Nearby.getMessagesClient(activity).subscribe(profileMessageListener);
    }
    public void unsubscribe() {
        Utilities.logToast(activity, "Unsubscribing...");
        Nearby.getMessagesClient(activity).unsubscribe(profileMessageListener);
    }
    public void publish() {
        profileMessage = new Message(MyProfile.singleton(activity).serialize().getBytes(StandardCharsets.UTF_8));
        Utilities.logToast(activity, "Publishing my profile...");
        Nearby.getMessagesClient(activity).publish(profileMessage).addOnFailureListener(e ->
                Utilities.logToast(activity, "API Error!"));
    }
    public void unpublish() {
        Utilities.logToast(activity, "Unpublishing...");
        Nearby.getMessagesClient(activity).unpublish(profileMessage);
    }
}
