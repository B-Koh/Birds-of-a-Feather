package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.DBSession;

import java.util.List;

public class SessionsViewAdapter extends RecyclerView.Adapter<SessionsViewAdapter.ViewHolder> {
    private List<DBSession> sessions;
    private final Context context;

    public SessionsViewAdapter (List<DBSession> sessions, Context context) {
        super();
        this.sessions = sessions;
        this.context = context;
    }

    @NonNull
    @Override
    public SessionsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.sessions_row, parent, false);

        return new ViewHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsViewAdapter.ViewHolder holder, int position) {
        holder.setSession(sessions.get(position));

    }

    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView sessionName;
        private final Context context;
        private final SessionsViewAdapter adapter;

        private DBSession session;

        ViewHolder(View itemView, Context context, SessionsViewAdapter adapter) {
            super(itemView);
            this.sessionName = itemView.findViewById(R.id.sessions_row_name);
            this.context = context;
            this.adapter = adapter;
        }

        public void setSession(DBSession session) {
            this.session = session;
            this.sessionName.setText(session.getSessionName());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, ViewSessionProfilesActivity.class);
            intent.putExtra("session_name", session.getSessionName());
            context.startActivity(intent);
        }
    }
}
