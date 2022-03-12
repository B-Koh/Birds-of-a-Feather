package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;
import com.example.birds_of_a_feather_team_20.model.db.DBSession;
import com.example.birds_of_a_feather_team_20.model.db.SessionDao;
import com.example.birds_of_a_feather_team_20.model.db.SessionDatabase;

import java.util.List;


public class ViewSessionsActivity extends AppCompatActivity {

    private RecyclerView sessionsRecyclerView;
    private RecyclerView.LayoutManager sessionsLayoutManager;
    private SessionsViewAdapter sessionsViewAdapter;

    private SessionDatabase db;
    private SessionDao sessionDao;
    private List<DBSession> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sessions);
        setTitle("Sessions List");

        Log.i(this.getClass().getSimpleName(), "Viewing sessions..");

        db = SessionDatabase.singleton(this);
        sessionDao = db.sessionDao();

        setSessions();
    }

    public void setSessions(){
        sessions = sessionDao.getAll();

        //Set up the recycler view to show our database contents
        sessionsRecyclerView = findViewById(R.id.course_view);
        sessionsLayoutManager = new LinearLayoutManager(this);
        sessionsRecyclerView.setLayoutManager(sessionsLayoutManager);
        sessionsViewAdapter = new SessionsViewAdapter(sessions, this);

        sessionsRecyclerView.setAdapter(sessionsViewAdapter);

    }

    public void onLaunchFriendsClicked(View view) {
        finish();
    }

    public void onLaunchFavoritesClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void onLaunchProfileClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void onLaunchDebugClicked(View view) {
        onLaunchFriendsClicked(view);
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

}
