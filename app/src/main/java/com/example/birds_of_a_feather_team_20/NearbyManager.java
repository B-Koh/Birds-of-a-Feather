package com.example.birds_of_a_feather_team_20;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.example.birds_of_a_feather_team_20.sorting.MatchComparator;
import com.example.birds_of_a_feather_team_20.sorting.MatchScoreSizeWeighted;
import com.example.birds_of_a_feather_team_20.sorting.MatchScoreTimeWeighted;
import com.example.birds_of_a_feather_team_20.sorting.ProfileComparator;
import com.example.birds_of_a_feather_team_20.sorting.SizeWeightComparator;
import com.example.birds_of_a_feather_team_20.sorting.TimeWeightComparator;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.concurrent.Executors;

/**
 * This class is responsible for handling the Nearby messaging.
 * It has methods for (un)subscribing and (un)publishing, and it handles the callback for finding
 * a message.
 */
public class NearbyManager {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private final Activity activity;
    private final ProfilesListView profilesListView;
    private final MessageListener profileMessageListener;

    private Message profileMessage;
    private boolean isScanning;

    public boolean getIsScanning() {
        return isScanning;
    }

    /**
     * Constructor. Sets up the message listener and the profiles list view
     */
    public NearbyManager(Activity activity) {
        this.activity = activity;
        isScanning = false;

        // Set up the list view of nearby profiles
        profilesListView = new ProfilesListView(activity);

        // Set up the MessageListener and its callbacks
        profileMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                if (message == null || !getIsScanning()) return;
                String msgBody = new String(message.getContent(), CHARSET);
//                activity.runOnUiThread(() -> {});
                Utilities.logToast(activity, "Found profile: " + msgBody);

                onFoundProfile(msgBody); // Handle the profile we found
            }

            // Only listening to this for debugging purposes. Not functionally necessary.
            @Override
            public void onLost(final Message message) {
                if (message == null || !getIsScanning()) return;
                String msgBody = new String(message.getContent(), CHARSET);
                Utilities.logToast(activity, "Lost profile: " + msgBody);
            }
        };
    }
    /**
     * Start listening for nearby profiles
     */
    private void subscribe() {
        Utilities.logToast(activity, "Subscribing to nearby profiles...");
        Nearby.getMessagesClient(activity).subscribe(profileMessageListener);
    }
    /**
     * Stop listening for nearby profiles
     */
    private void unsubscribe() {
        Utilities.logToast(activity, "Unsubscribing...");
        Nearby.getMessagesClient(activity).unsubscribe(profileMessageListener);
    }
    /**
     * Start sharing our profile
     */
    private void publish() {
        Utilities.logToast(activity, "Publishing my profile...");
        // Convert our profile to a string and publish it as a message
        profileMessage = new Message(MyProfile.singleton(activity).serialize().getBytes(CHARSET));
        Nearby.getMessagesClient(activity).publish(profileMessage).addOnFailureListener(e ->
                Utilities.logToast(activity, "API Error!"));
    }
    /**
     * Stop sharing our profile
     */
    private void unpublish() {
        Utilities.logToast(activity, "Unpublishing...");
        Nearby.getMessagesClient(activity).unpublish(profileMessage);
    }

    /**
     * Call this method when a profile is found. If the profile is new, then it will be added to
     * the list of profiles. If it isn't new, then the existing profile will be updated with any
     * new data (e.g. a new name, photo, etc.)
     */
    private void onFoundProfile(String profileData) {
        Utilities.logToast(activity, "Adding profile: " + profileData);
        if (profileData == null) return;

        // Convert the string to a Profile
        Profile profile = Profile.deserialize(profileData);
        if (profile == null || !profile.isValid())
            return;

        // Count the number of matching courses
        int courseMatches = profile.countMatchingCourses(MyProfile.singleton(activity));
        Utilities.logToast(activity, profile.getName() + " has " + courseMatches
                + " matching courses.");

        // Store the Profile in our list of profiles
        ProfilesCollection profiles = ProfilesCollection.singleton();
        profiles.addOrUpdateProfile(profile, courseMatches);
        profilesListView.refreshProfileListView(profiles.getModifications(), profiles.getAdditions(), profiles.getMovements());
    }



    /**
     * Send a mock message (for testing purposes)
     */
    public void sendFakeMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(CHARSET));
        getProfileMessageListener().onFound(message);
        getProfileMessageListener().onLost(message);
    }

    /**
     * Send a mock message (for testing purposes)
     */
    public void sendFakeMessage(Activity activity, Profile profile) {
//        Executors.newSingleThreadExecutor().submit(() -> {
            String messageStr = profile.serialize();
            sendFakeMessage(messageStr);
//        });
//        activity.runOnUiThread(() -> {
//            String messageStr = profile.serialize();
//            sendFakeMessage(messageStr);
//        });
    }

    /**
     * Get the message listener
     */
    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }

    /**
     * Begin the scanning for Nearby
     * @return false if already started
     */
    public boolean startScanning() {
        if (getIsScanning()) return false; // failure, already started

        isScanning = true;
        Utilities.logToast(activity,"Starting...");
        subscribe();
        publish();
        return true;
    }

    /**
     * Stop the scanning for Nearby
     * @return false if already stopped
     */
    public boolean stopScanning() {
        if (!getIsScanning()) return false; // failure, already stopped

        isScanning = false;
        Utilities.logToast(activity, "Stopping...");
        unsubscribe();
        unpublish();
        return true;
    }

    public void republish() {
        if (getIsScanning()) {
            Utilities.logToast(activity, "Republishing...");
            unpublish();
            publish();
        }
    }

    public void changeSort(String sortType) {
        // UPDATE LIST
        ProfilesCollection coll = ProfilesCollection.singleton();
        ProfileComparator comp = new MatchComparator(MyProfile.singleton(activity));
        if (sortType.equals("Recent")) {
            comp = new TimeWeightComparator("WI", 2022, MyProfile.singleton(activity));
        } else if (sortType.equals("Class Size")) {
            comp = new SizeWeightComparator(MyProfile.singleton(activity));
        }
        coll.changeSort(comp);
        // UPDATE ADAPTER - TODO
        profilesListView.refreshProfileListView(coll.getModifications(), coll.getAdditions(), coll.getMovements());
    }

    public void refreshList() {
        ProfilesCollection coll = ProfilesCollection.singleton();
        profilesListView.refreshProfileListView(coll.getModifications(), coll.getAdditions(), coll.getMovements());
    }
}
