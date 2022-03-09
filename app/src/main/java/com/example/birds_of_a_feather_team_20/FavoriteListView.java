package com.example.birds_of_a_feather_team_20;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListView {

    private List<Profile> favorites;
    private ProfilesViewAdapter adapter;

    public FavoriteListView(Activity activity) {
        this.favorites = filterFavorites(ProfilesCollection.singleton().getProfiles());
        this.adapter = new ProfilesViewAdapter(activity, favorites);

        RecyclerView basicRecycler = activity.findViewById(R.id.profile_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        basicRecycler.setLayoutManager(layoutManager);
        basicRecycler.setAdapter(adapter);

        //Log.d("favorites", String.valueOf(favorites.size()) );
        //Log.d("favorites1", String.valueOf(filterFavorites(favorites).size()) );
    }

    private static List<Profile> filterFavorites(List<Profile> profiles) {
        List<Profile> favorites = new ArrayList<>();

        for (Profile profile : profiles) {
            if (profile.isFavorite()) {
                favorites.add(profile);
            }
        }

        return favorites;
    }
}
