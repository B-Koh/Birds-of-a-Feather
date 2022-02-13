package com.example.birds_of_a_feather_team_20;

import android.app.Activity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NearbyManager {

    private final List<Profile> foundProfiles;
    private final Stack<Integer> additions;
    private final Stack<Integer> modifications;

//    RecyclerView basicRecycler;
//    RecyclerView.LayoutManager layoutManager;
//    ProfilesViewAdapter adapter;

    private final Activity activity;
    private final NearbyProfilesListView profilesListView;
    private final MessageListener profileMessageListener;
    private Message profileMessage;

    public static final Charset CHARSET = StandardCharsets.UTF_8;


    public NearbyManager(Activity activity) {
        this.activity = activity;

        foundProfiles = new ArrayList<>();
        additions = new Stack<>();
        modifications = new Stack<>();

        profilesListView = new NearbyProfilesListView(activity);

        profileMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                String msgBody = new String(message.getContent(), CHARSET);
                Utilities.logToast(activity, "Found profile: " + msgBody);
                onFoundProfile(msgBody);
            }

            @Override
            public void onLost(final Message message) {
                String msgBody = new String(message.getContent(), CHARSET);
                Utilities.logToast(activity, "Lost profile: " + msgBody);
            }
        };
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
        profileMessage = new Message(MyProfile.singleton(activity).serialize().getBytes(CHARSET));
        Utilities.logToast(activity, "Publishing my profile...");
        Nearby.getMessagesClient(activity).publish(profileMessage).addOnFailureListener(e ->
                Utilities.logToast(activity, "API Error!"));
    }
    public void unpublish() {
        Utilities.logToast(activity, "Unpublishing...");
        Nearby.getMessagesClient(activity).unpublish(profileMessage);
    }

    public void sendFakeMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(CHARSET));
        profileMessageListener.onFound(message);
        profileMessageListener.onLost(message);
    }

    private void onFoundProfile(String profileData) {
        // A profile was received, now process it
        Profile profile = Profile.deserialize(profileData);
        if (profile == null) return;
        addProfile(profile);
        profilesListView.refreshProfileListView(getModifications(), getAdditions());
    }

    private void addProfile(Profile profile) {
        /*for (int i = 0, nearbyProfilesSize = foundProfiles.size(); i < nearbyProfilesSize; i++) {
            Profile p = foundProfiles.get(i);
            if (p.getId().equals(profile.getId())) {
                if (!p.getName().equals(profile.getName()) || !p.getPhotoURL().equals(profile.getPhotoURL())) {
                    updateExistingProfile(profile, i);
                }
                // Don't want to notify the recycler if nothing changed (because it will play an animation)
                return;
            }
        }*/
        if (profile == null) return;
        int index = getFoundProfiles().indexOf(profile);
        if (index == -1) {

            insertNewProfile(profile, getFoundProfiles().size());
        }
        else {
            updateExistingProfile(profile, index);
        }
    }

    private void updateExistingProfile(Profile newProfile, int index) {
        Utilities.logToast(activity, "Update existing profile: " + newProfile.getName());

        Profile existing = getFoundProfiles().get(index);
        if(newProfile == null || existing == null) {
            return; // Note: Making your profile null will not remove it from others' lists
        }
        if (existing.strongEquals(newProfile)) return; // Don't update if they already match (would unnecessarily update the list view)
        existing.updateProfile(newProfile);
        getModifications().add(index);
    }
    private void insertNewProfile(Profile profile, int index) {
        Utilities.logToast(activity, "Adding to List: " + profile.serialize());
        getAdditions().add(index);
        getFoundProfiles().add(index, profile); // no profile with this id, so add it
        // Note, not sure if it is necessary to tell the adapter that all the other items moved down
    }

    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }

    public ProfilesCollection getFoundProfiles() {
//        return foundProfiles;
        return ProfilesCollection.singleton();
    }

    public Stack<Integer> getAdditions() {
        return additions;
    }

    public Stack<Integer> getModifications() {
        return modifications;
    }
}
