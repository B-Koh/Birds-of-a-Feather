package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class NearbyManager {
    private static Message profileMessage;
    private static MessageListener messageListener;
    public static ArrayList<Profile> nearbyProfiles;
    public static Stack<Integer> additions;
    public static Stack<Integer> modifications;

    /**
     * Just for debugging purposes
     */
    public static MessageListener getMessageListener() { return messageListener; }

    protected static void recordProfile(Profile profile) {
        for (int i = 0, nearbyProfilesSize = nearbyProfiles.size(); i < nearbyProfilesSize; i++) {
            Profile p = nearbyProfiles.get(i);
            if (p.getId().equals(profile.getId())) {
                if (!p.getName().equals(profile.getName()) || !p.getPhotoURL().equals(profile.getPhotoURL())) {
                    Log.i("Update existing profile", p.getName());
                    modifications.add(i);
                    p.setName(profile.getName()); // update name if id matches
                    p.setPhotoURL(profile.getPhotoURL()); // update url if id matches
                }
                // Don't want to notify the recycler if nothing changed (because it will play an animation)
                return;
            }
        }
        Log.i("Adding to List", profile.getName());
        additions.add(nearbyProfiles.size());
        nearbyProfiles.add(profile); // no profile with this id, so add it
//        profile.getThumbnail(); // FIXME cannot do this here
    }
    protected static void updateMessage(Context context) {
        if (nearbyProfiles == null) {
            nearbyProfiles = new ArrayList<Profile>();
        }
        if (additions == null) {
            additions = new Stack<>();
        }
        if (modifications == null) {
            modifications = new Stack<>();
        }
        /*if (messageListener == null) {
            messageListener = new MessageListener() {
                @Override
                public void onFound(Message message) {
                    Log.d("    NEARBY", "Found message: " + new String(message.getContent()));
                    Profile foundProfile = Profile.deserialize(new String(message.getContent()));
                    recordProfile(foundProfile);
                }
                @Override
                public void onLost(Message message) {
                    Log.d("    NEARBY", "Lost sight of message: " + new String(message.getContent()));
                }
            };
        }
        profileMessage = new Message(MyProfile.singleton(context.getApplicationContext()).serialize()
                .getBytes(StandardCharsets.UTF_8));*/
    }
}
