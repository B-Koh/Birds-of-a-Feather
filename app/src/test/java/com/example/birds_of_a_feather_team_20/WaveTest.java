package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import com.example.birds_of_a_feather_team_20.wave.WaveManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class WaveTest {
    @Test
    public void testSetter() {
        Profile profile = new Profile("Name", "Url", "id");
        assert !profile.getWavedAtMe();
        profile.setWavedAtMe(true);
        assert profile.getWavedAtMe();
    }

    @Test
    public void testMakeWaveMessage() {
        WaveManager waveManager = new WaveManager("my_profile_id");

        Profile profile0 = new Profile("Name0", "Url", "id0");
        Profile profile1 = new Profile("Name1", "Url", "id1");
        Profile profile2 = new Profile("Name2", "Url", "id2");

        String expected = WaveManager.WAVE_HEADER +
                        "\n" +
                        "my_profile_id" +
                        "\n";
        assertEquals(expected, waveManager.makeWaveMessage());

        waveManager.addWave(profile0);
        expected += "id0\n";
        assertEquals(expected, waveManager.makeWaveMessage());

        waveManager.addWave(profile1);
        expected += "id1\n";
        assertEquals(expected, waveManager.makeWaveMessage());

        waveManager.addWave(profile2);
        expected += "id2\n";
        assertEquals(expected, waveManager.makeWaveMessage());

        waveManager.removeWave(profile0);
        waveManager.removeWave(profile1);
        waveManager.removeWave(profile2);

        expected = WaveManager.WAVE_HEADER +
                "\n" +
                "my_profile_id" +
                "\n";
        assertEquals(expected, waveManager.makeWaveMessage());
    }

    @Test
    public void isWaveMessage() {
        WaveManager waveManager = new WaveManager("my_profile_id");
        assert waveManager.isWaveMessage(waveManager.makeWaveMessage());
        waveManager.addWave(new Profile("name0", "url0", "id0"));
        assert waveManager.isWaveMessage(waveManager.makeWaveMessage());
        waveManager.addWave(new Profile("name1", "url0", "id1"));
    }

    @Test
    public void testWavedAtMe() {
        WaveManager waveManager = new WaveManager("727610921");
        String str = "Wave:\n" +
                "99a32c35-5091-4d7f-a1e3-fe0bfe4b1334\n" +
                "727610921";
        assert waveManager.someoneWavedAtMe(str);
    }

    @Test
    public void testParse() {
        WaveManager myWaveManager = new WaveManager("my_profile_id");
        Profile George = new Profile("Name0", "Url", "george_id");
        myWaveManager.addWave(George);
        myWaveManager.addWave(new Profile("name", "url", "another_id"));
        String message = myWaveManager.makeWaveMessage();

        WaveManager georgeWaveManager = new WaveManager("george_id");
        assert georgeWaveManager.someoneWavedAtMe(message);
        assertEquals("my_profile_id", georgeWaveManager.whoWavedAtMe(message));
    }
}
