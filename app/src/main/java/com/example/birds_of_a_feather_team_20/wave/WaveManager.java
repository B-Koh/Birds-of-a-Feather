package com.example.birds_of_a_feather_team_20.wave;

public class WaveManager {
    private static String WAVE_HEADER = "Wave:";
    public static boolean isWaveMessage(String messageStr) {
        String[] split = messageStr.split(" ");
        return split[0].equals(WAVE_HEADER);
    }
//    public static String makeWaveMessage
}
