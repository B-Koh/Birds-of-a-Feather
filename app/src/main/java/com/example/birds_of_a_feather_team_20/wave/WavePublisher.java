package com.example.birds_of_a_feather_team_20.wave;

import android.content.Context;
import android.util.Log;

import com.example.birds_of_a_feather_team_20.MyProfile;
import com.example.birds_of_a_feather_team_20.NearbyManager;
import com.example.birds_of_a_feather_team_20.Profile;
import com.example.birds_of_a_feather_team_20.Utilities;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.util.function.Consumer;

public class WavePublisher {
    private final WaveManager waveManager;
    private Message waveMessage;
    private boolean isPublished = false;

    private static WavePublisher singletonInstance;

    public static WavePublisher singleton(Context context) {
        if (singletonInstance == null)
            singletonInstance = new WavePublisher(context);
        return singletonInstance;
    }

    public WavePublisher(Context context) {
        this.waveManager = new WaveManager(MyProfile.singleton(context).getId());
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public void sendWave(Profile profile, Context context, Consumer<Boolean> callback) {
        waveManager.addWave(profile);
        unpublish(context, success -> {
            if (success)
                publish(waveManager.makeWaveMessage(), context, callback);
        });
    }

    private void publish(String strWaveMessage, Context context, Consumer<Boolean> callback) {
        if (!waveManager.isWaveMessage(strWaveMessage) || isPublished) {
            callback.accept(false);
            return;
        }
        waveMessage = new Message(strWaveMessage.getBytes(NearbyManager.CHARSET));
        Nearby.getMessagesClient(context).publish(waveMessage).addOnFailureListener(e ->
        {
            Utilities.logToast(context, "API Error! (Wave)");
            callback.accept(false);
        }).addOnSuccessListener(e -> {
//            Utilities.logToast(context, "Wave sent!");
            Log.d("Wave", "Sent wave message:\n" + new String(waveMessage.getContent(), NearbyManager.CHARSET));
            isPublished = true;
            callback.accept(true);
        });
    }

    private void unpublish(Context context, Consumer<Boolean> callback) {
        if (!isPublished || waveMessage == null) {
            Log.d("Wave", "Unpublish: nothing published.");
            callback.accept(true);
            return;
        }
        Nearby.getMessagesClient(context).unpublish(waveMessage).addOnSuccessListener(e -> {
            Log.d("Wave", "Unpublished successfully.");
            isPublished = false;
            callback.accept(true);
        }).addOnFailureListener(e -> callback.accept(false));
    }

    public void finalize(Context context) {
        unpublish(context, x -> {});
    }
}
