package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_20.model.db.SessionDao;
import com.example.birds_of_a_feather_team_20.model.db.SessionDatabase;

import java.util.List;

public class ViewSessionProfilesActivity extends AppCompatActivity {
    private RecyclerView profilesRecyclerView;
    private RecyclerView.LayoutManager profilesLayoutManager;
    private ProfilesViewAdapter profilesViewAdapter;

    private SessionDatabase db;
    private SessionDao sessionDao;
    private List<Profile> profiles;
    private String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sessions);
        Intent intent = getIntent();

        sessionName = intent.getStringExtra("session_name");

        db = SessionDatabase.singleton(this);
        sessionDao = db.sessionDao();

        setProfiles();
    }

    public void setProfiles(){
        profiles =  sessionDao.getProfilesInSession(sessionName);

        //Set up the recycler view to show our database contents
        profilesRecyclerView = findViewById(R.id.course_view);
        profilesLayoutManager = new LinearLayoutManager(this);
        profilesRecyclerView.setLayoutManager(profilesLayoutManager);
        profilesViewAdapter = new ProfilesViewAdapter(this, profiles);

        profilesRecyclerView.setAdapter(profilesViewAdapter);

    }

    public void onReturnToSessionsClicked(View view) {
        Intent intent = new Intent(this, ViewSessionsActivity.class);
        startActivity(intent);
    }
}
