package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearbyManager {
    private static final String TAG = "BIRDS OF A FEATHER!";
    private static Message profileMessage;
    private static MessageListener messageListener;
    public static ArrayList<Profile> nearbyProfiles;

    /*public static void setupMessage(Context context) { // TODO will need to set up again (and maybe unpublish old + publish new) when the user changes their name or photoURL
        messageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };
    }*/


    public static void startNearby(Context context) { // When is onStart invoked?
        updateMessage(context);
        Nearby.getMessagesClient(context.getApplicationContext()).publish(profileMessage);
        Nearby.getMessagesClient(context.getApplicationContext()).subscribe(messageListener);
    }
    public static void stopNearby(Context context) { // FIXME: we may want to continue publishing, per the directions
        Nearby.getMessagesClient(context.getApplicationContext()).unpublish(profileMessage);
        Nearby.getMessagesClient(context.getApplicationContext()).unsubscribe(messageListener);
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

                    // TODO - HANDLE MESSAGE HERE:
                    Profile foundProfile = new Profile(null,null, null);
                    foundProfile.deserialize(new String(message.getContent()));
                    for (Profile profile : nearbyProfiles) {
                        if (profile.getId().equals(foundProfile.getId())) {
                            profile.setName(foundProfile.getName());
                            profile.setPhotoURL(foundProfile.getPhotoURL());
                            return;
                        }
                    }
                    nearbyProfiles.add(foundProfile);
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
