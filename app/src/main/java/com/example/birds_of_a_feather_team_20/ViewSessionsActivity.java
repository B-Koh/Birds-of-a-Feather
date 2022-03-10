package com.example.birds_of_a_feather_team_20;

import android.os.Bundle;
import android.se.omapi.Session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import java.util.List;


public class ViewSessionsActivity extends AppCompatActivity {

    private RecyclerView sessionsRecyclerView;
    private RecyclerView.LayoutManager sessionsLayoutManager;
//    private SessionsViewAdapter sessionsViewAdapter;

//    private SessionDatabase db;
//    private SessionDao sessionDao;
//    private List<DBSession> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sessions);

//        db = Room.inMemoryDatabaseBuilder(context, SessionDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        sessionDao = db.sessionDao();

//        setSessions();
    }

    public void setSessions(){
//        sessions = sessionDao.getAll();

        //Set up the recycler view to show our database contents
        sessionsRecyclerView = findViewById(R.id.course_view);
        sessionsLayoutManager = new LinearLayoutManager(this);
        sessionsRecyclerView.setLayoutManager(sessionsLayoutManager);
//        sessionsViewAdapter = new SessionsViewAdapter(sessions);

//        sessionsRecyclerView.setAdapter(sessionsViewAdapter);

    }

}
