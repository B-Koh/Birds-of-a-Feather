package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NearbyManager {
    private static final String TAG = "BIRDS OF A FEATHER!";
    private static Message profileMessage;
    private static MessageListener messageListener;
    public static ArrayList<Profile> nearbyProfiles;

    /**
     * Just for debugging purposes
     */
    public static MessageListener getMessageListener() { return messageListener; }

    public static void startNearby(Context context) {
        updateMessage(context);
        Nearby.getMessagesClient(context.getApplicationContext()).publish(profileMessage);
        Nearby.getMessagesClient(context.getApplicationContext()).subscribe(messageListener);
    }
    public static void stopNearby(Context context) { // FIXME: we may want to continue publishing, per the directions
        Nearby.getMessagesClient(context.getApplicationContext()).unpublish(profileMessage);
        Nearby.getMessagesClient(context.getApplicationContext()).unsubscribe(messageListener);
    }
    protected static void recordProfile(Profile profile) {
        for (Profile p : nearbyProfiles) {
            if (p.getId().equals(profile.getId())) {
                p.setName(profile.getName()); // update name if id matches
                p.setPhotoURL(profile.getPhotoURL()); // update url if id matches
                return;
            }
        }
        nearbyProfiles.add(profile); // no profile with this id, so add it
    }
    protected static void updateMessage(Context context) {
        if (nearbyProfiles == null) {
            nearbyProfiles = new ArrayList<Profile>();
        }
        if (messageListener == null) {
            messageListener = new MessageListener() {
                @Override
                public void onFound(Message message) {
                    Log.d(TAG, "Found message: " + new String(message.getContent()));
                    Profile foundProfile = new Profile(null,null, null);
                    foundProfile.deserialize(new String(message.getContent()));
                    recordProfile(foundProfile);
                }
                @Override
                public void onLost(Message message) {
                    Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
                }
            };
        }
        profileMessage = new Message(MyProfile.singleton(context.getApplicationContext()).serialize()
                .getBytes(StandardCharsets.UTF_8));
    }
}
