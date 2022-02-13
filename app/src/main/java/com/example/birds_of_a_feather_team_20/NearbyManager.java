package com.example.birds_of_a_feather_team_20;

import android.app.Activity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    /**
     * Constructor. Sets up the message listener and the profiles list view
     */
    public NearbyManager(Activity activity) {
        this.activity = activity;

        // Set up the list view of nearby profiles
        profilesListView = new ProfilesListView(activity);

        // Set up the MessageListener and its callbacks
        profileMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                if (message == null) return;
                String msgBody = new String(message.getContent(), CHARSET);
                Utilities.logToast(activity, "Found profile: " + msgBody);

                onFoundProfile(msgBody); // Handle the profile we found
            }

            // Only listening to this for debugging purposes. Not functionally necessary.
            @Override
            public void onLost(final Message message) {
                if (message == null) return;
                String msgBody = new String(message.getContent(), CHARSET);
                Utilities.logToast(activity, "Lost profile: " + msgBody);
            }
        };
    }
    /**
     * Start listening for nearby profiles
     */
    public void subscribe() {
        Utilities.logToast(activity, "Subscribing to nearby profiles...");
        Nearby.getMessagesClient(activity).subscribe(profileMessageListener);
    }
    /**
     * Stop listening for nearby profiles
     */
    public void unsubscribe() {
        Utilities.logToast(activity, "Unsubscribing...");
        Nearby.getMessagesClient(activity).unsubscribe(profileMessageListener);
    }
    /**
     * Start sharing our profile
     */
    public void publish() {
        Utilities.logToast(activity, "Publishing my profile...");
        // Convert our profile to a string and publish it as a message
        profileMessage = new Message(MyProfile.singleton(activity).serialize().getBytes(CHARSET));
        Nearby.getMessagesClient(activity).publish(profileMessage).addOnFailureListener(e ->
                Utilities.logToast(activity, "API Error!"));
    }
    /**
     * Stop sharing our profile
     */
    public void unpublish() {
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

        // Store the Profile in our list of profiles
        ProfilesCollection profiles = ProfilesCollection.singleton();
        profiles.addOrUpdateProfile(profile);
        profilesListView.refreshProfileListView(profiles.getModifications(), profiles.getAdditions());
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
     * Get the message listener
     */
    public MessageListener getProfileMessageListener() {
        return profileMessageListener;
    }
}
