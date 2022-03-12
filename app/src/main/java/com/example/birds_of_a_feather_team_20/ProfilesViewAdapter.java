package com.example.birds_of_a_feather_team_20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * View adapter for the UI list of nearby profiles. Binds profiles to the view holders.
 */
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

        return new ViewHolder(view, activity, profiles);
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

        //private Profile profile;
        private int index;

        private final TextView profileNameText;
        private final TextView urlText;
        private final ImageView photo;
        private final ImageButton favorite;
        private final ImageButton waved;

        ViewHolder(View itemView, Context context, List<Profile> profiles) {
            super(itemView);
            this.profileNameText = itemView.findViewById(R.id.profile_name);
            this.urlText = itemView.findViewById(R.id.profile_photo_url);
            this.photo = itemView.findViewById(R.id.profile_photo);
            this.favorite = itemView.findViewById(R.id.set_favorite_button);
            this.waved = itemView.findViewById(R.id.waved_icon);
            itemView.setOnClickListener(this);

            //            Profile profile = profiles.get(index);

            // Updates graphic before click
//            updateFavoriteGraphic(button, context, profile.getIsFavorite());
//
//            updateWavedGraphic(this.waved, profile.getWavedAtMe());
            updateWavedGraphic(this.waved, false); // set it false until everything loads
            this.favorite.setVisibility(View.INVISIBLE);

            // On favorite click, set drawable and logic to favorite/unfavorite
            this.favorite.setOnClickListener((View view) -> {
                Profile thisProfile = profiles.get(index);
                if (!thisProfile.getIsFavorite()) {
                    thisProfile.setFavorite();
                }
                else {
                    thisProfile.unFavorite();
                }
                // Updates graphic after click
                updateFavoriteGraphic(this.favorite, context, thisProfile.getIsFavorite());
                //ProfilesCollection.singleton().getModifications().add(ProfilesCollection.singleton().getProfiles().indexOf(thisProfile));
            });
        }

        private void updateWavedGraphic(ImageView wavedIcon, boolean waved) {
            if (waved) {
                wavedIcon.setVisibility(View.VISIBLE);
            } else {
                wavedIcon.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * Updates the image button of star when it is clicked
         * @param imagebutton - button that is clicked
         * @param context - context where button is clicked
         * @param favorited - checks if favorited or not
         */
        private void updateFavoriteGraphic(ImageButton imagebutton, Context context, boolean favorited) {
            imagebutton.setVisibility(View.VISIBLE);
            if (favorited) {
                imagebutton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite2));
            }
            else {
                imagebutton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite));
            }
        }

        public void setProfile(Activity activity, Profile profile, int index) {
            ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();

            // Refresh on background thread
            Future<Void> future = backgroundThreadExecutor.submit(() -> {
                profile.fetchThumbnail();
                activity.runOnUiThread(() -> {
                    this.index = index;
                    //this.profile = profile;
                    this.profileNameText.setText(profile.getName());
//                    this.urlText.setText(profile.getPhotoURL());
                    this.urlText.setText(Utilities.coursesToString(profile.getCourses()));
                    this.photo.setImageBitmap(profile.getPrefetchedThumbnail());

                    updateFavoriteGraphic(this.favorite, activity, profile.getIsFavorite());
                    updateWavedGraphic(this.waved, profile.getWavedAtMe());
                });
                return null;
            });
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, DisplayProfile.class);
            intent.putExtra("index_in_profilesList", this.index);
            context.startActivity(intent);
        }


    }
}