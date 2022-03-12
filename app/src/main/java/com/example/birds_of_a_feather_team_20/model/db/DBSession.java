package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBSession {
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "session_id")
    public int dbSessionId;
    //@ColumnInfo(name = "session_name")
    public String sessionName;

    public DBSession(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getSessionId() { return dbSessionId; }

    public void setSessionId(int sessionId) { this.dbSessionId = sessionId; }

    public String getSessionName() { return sessionName; }

    public void setSessionName(String sessionName) { this.sessionName = sessionName; }
}

