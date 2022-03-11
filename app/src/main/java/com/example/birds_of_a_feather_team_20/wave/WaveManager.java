package com.example.birds_of_a_feather_team_20.wave;

import android.util.Log;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Wave messages will look like:
 * Wave:
 * senderId
 * id1
 * id2
 * id3
 *
 * Where senderId is the id of the sender, and
 * id1..idn are the ids of the profiles which the sender waved to
 */
public class WaveManager {

    public static String WAVE_HEADER = "Wave:";

    private List<Profile> waveRecipients;

    private String myId;

    public WaveManager(String myId) {
        this.myId = myId;
        waveRecipients = new ArrayList<>();
    }

    public void addWave(Profile profile) {
        if (!waveRecipients.contains(profile))
            waveRecipients.add(profile);
    }
    public void removeWave(Profile profile) {
        waveRecipients.remove(profile);
    }
    public String makeWaveMessage() {
        StringBuilder msg = new StringBuilder(WAVE_HEADER).append("\n");
        msg.append(myId).append("\n");
        for (Profile recipient : waveRecipients) {
            msg.append(recipient.getId()).append("\n");
        }
        return msg.toString();
    }
    private String parseSenderId(String message) {
        String[] split = message.split("\n");
        return split[1];
    }
    private List<String> parseRecipientIds(String message) {
        String[] split = message.split("\n");
        ArrayList<String> recipientIds = new ArrayList<>(split.length - 2);
        for (int i = 2; i < split.length; i++) {
            recipientIds.add(split[i]);
        }
        return recipientIds;
    }

    public boolean someoneWavedAtMe(String message) {
        return parseRecipientIds(message).contains(myId);
    }

    public String whoWavedAtMe(String message) {
        if (someoneWavedAtMe(message))
            return parseSenderId(message);
        else
            return null;
    }

    public boolean isWaveMessage(String messageStr) {
        String[] split = messageStr.split("\n");
        return split[0].equals(WAVE_HEADER) && split.length > 1; // Should have sender ID
    }
}
