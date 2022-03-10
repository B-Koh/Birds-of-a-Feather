package com.example.birds_of_a_feather_team_20;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the favorite tabs view and manages it's recycler. Also contains a helper
 * method filterFavorites that filters the current profiles list to a list of favorited profiles.
 */
public class FavoriteListView {

    private List<Profile> favorites;
    private ProfilesViewAdapter adapter;

    /**
     * Handles the recycler view of the Favorites list
     * @param activity - activity that holds recycler
     */
    public FavoriteListView(Activity activity) {
        this.favorites = filterFavorites(ProfilesCollection.singleton().getProfiles());
        this.adapter = new ProfilesViewAdapter(activity, favorites);

        RecyclerView basicRecycler = activity.findViewById(R.id.profile_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        basicRecycler.setLayoutManager(layoutManager);
        basicRecycler.setAdapter(this.adapter);

        //Log.d("favorites", String.valueOf(favorites.size()) );
        //Log.d("favorites1", String.valueOf(filterFavorites(favorites).size()) );
    }

    /**
     * Filters all current profiles that are favorited
     * @param profiles - all profiles in current list
     * @return - list of profiles that are favorited
     */
    public List<Profile> filterFavorites(List<Profile> profiles) {
        List<Profile> favorites = new ArrayList<>();

        for (Profile profile : profiles) {
            if (profile.getIsFavorite()) {
                favorites.add(profile);
            }
        }
        return favorites;
    }
}
