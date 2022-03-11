package com.example.birds_of_a_feather_team_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.wave.WaveManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DebugActivity extends AppCompatActivity {

    private static List<String> profiles;
    public static List<String> messagesToAdd() {
        if (profiles == null) {
            profiles = new ArrayList<>();
        }
        return profiles;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        setTitle("Debug Tools");
//        profilesToAdd().clear();
    }
    public void onSetFakeProfileClicked(View view) {
        TextView nameView = (TextView)findViewById(R.id.debug_textview);
        String debugText = nameView.getText().toString();
        if (debugText.equals("")) return;

        try {
            InputStream in = new ByteArrayInputStream(debugText.getBytes(StandardCharsets.UTF_8));
            CSVParser parser = new CSVParser(in);
            List<String[]> thisList = parser.read();
            String name = ((String[]) thisList.get(0))[0];
            String url = ((String[]) thisList.get(1))[0];
            Profile profile = new Profile(name, url, String.valueOf(new Random().nextInt()));
            for (int i = 2; i < thisList.size(); i++) {
                int year = Integer.parseInt(((String[]) thisList.get(i))[0]);
                Course course = new Course(year, ((String[]) thisList.get(i))[1], ((String[]) thisList.get(i))[2], ((String[]) thisList.get(i))[3]);
                profile.addCourse(course);
            }
            messagesToAdd().add(profile.serialize());
//            nearbyManager.sendFakeMessage(this, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onClickedAddMyOwnProfile(View view) {
        Profile myProfile = MyProfile.singleton(this);
        Profile copy = new Profile(myProfile.getName(), myProfile.getPhotoURL(), String.valueOf(new Random().nextInt()));
        copy.setCourses(myProfile.getCourses());
        messagesToAdd().add(copy.serialize());
    }
    public void onClickedGenerateProfile(View view) {
        Profile myProfile = MyProfile.singleton(this);
        Profile copy = Utilities.generateProfile(this);
        messagesToAdd().add(copy.serialize());
    }
    public void onClickedGenerateWave(View view) {
//        Profile myProfile = MyProfile.singleton(this);
//        Profile copy = Utilities.generateProfile(this);
//        profilesToAdd().add(copy);
        Profile p = (Utilities.pickRandomProfile());
        if (p == null)
            return;
        WaveManager manager = new WaveManager(p.getId());
        manager.addWave(MyProfile.singleton(this));
        Log.d("Wave", "Picked For Wave - " + p.getName());
        messagesToAdd().add(manager.makeWaveMessage());

    }

    public void onLaunchProfileClicked(View view) {
        onLaunchFriendsClicked(view); // return to MainActivity first
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void onLaunchFavoritesClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void onLaunchFriendsClicked(View view) {
        finish();
    }
}