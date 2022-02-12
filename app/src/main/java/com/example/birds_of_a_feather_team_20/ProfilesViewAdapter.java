package com.example.birds_of_a_feather_team_20;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ProfilesViewAdapter extends RecyclerView.Adapter<ProfilesViewAdapter.ViewHolder> {
    private final List<Profile> profiles;
    private Activity activity;

    public ProfilesViewAdapter(Activity activity, List<Profile> profilesList) {
        super();
        this.activity = activity;
        this.profiles = profilesList;
    }

    @NonNull
    @Override
    public ProfilesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.profile_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        if(profiles == null || index >= profiles.size()) return;
        holder.setProfile(activity, profiles.get(index), index);
    }

    @Override
    public int getItemCount() {
        if (profiles == null) return 0;
        return this.profiles.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Profile profile;
        private int index;

        private final TextView profileNameText;
        private final TextView urlText;
        private final ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            // TODO also collect other text/image views
            this.profileNameText = itemView.findViewById(R.id.profile_name);
            this.urlText = itemView.findViewById(R.id.profile_photo_url);
            this.photo = itemView.findViewById(R.id.profile_photo);

            itemView.setOnClickListener(this);
        }

        public void setProfile(Activity activity, Profile profile, int index) {
            ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();

            // Refresh on background thread
            Future<Void> future = backgroundThreadExecutor.submit(() -> {
                profile.getThumbnail();
                activity.runOnUiThread(() -> {
                    this.index = index;
                    this.profile = profile;
                    this.profileNameText.setText(profile.getName());
                    this.urlText.setText(profile.getPhotoURL());
                    this.photo.setImageBitmap(profile.getThumbnail());
                });
                return null;
            });
        }

        @Override
        public void onClick(View view) {
            // TODO: Open profile details
//            Context context = view.getContext();
//            Intent intent = new Intent(context, ProfileDetailActivity.class);
//            intent.putExtra("index_in_profilesList", this.index);
//            context.startActivity(intent);
        }


    }
}