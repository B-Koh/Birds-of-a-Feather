package com.example.birds_of_a_feather_team_20;


import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;

/**
 * This class is for managing the UI of the profile list. It sets up the RecyclerView and
 * ViewAdapter, and it notifies the ViewAdapter when items have been added or updated.
 */
public class ProfilesListView {
    private final Activity activity;
    private final ProfilesViewAdapter adapter;
    private final List<Profile> foundProfiles;

    /**
     * Constructor. Sets up the UI components.
     * @param activity
     */
    public ProfilesListView(Activity activity) {
        this.activity = activity;
        this.foundProfiles = ProfilesCollection.singleton().getProfiles();
//        this.nearbyManager = mainActivity.getNearbyManager();

        RecyclerView basicRecycler = activity.findViewById(R.id.profile_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        basicRecycler.setLayoutManager(layoutManager);
        adapter = new ProfilesViewAdapter(activity, foundProfiles);
        basicRecycler.setAdapter(adapter);
    }

    /**
     * Notifies the adapter of any changes to the data
     */
    public void notifyAdapter(Stack<Integer> modifications, Stack<Integer> additions) {

        while(!modifications.isEmpty()) {
            Integer i = modifications.pop();
            if (i != null)
                adapter.notifyItemChanged(i);
        }
        while(!additions.isEmpty()) {
            Integer i = additions.pop();
            if (i != null)
                adapter.notifyItemInserted(i);
        }
    }

    /**
     * Fetches any new thumbnails on the modified profiles. Do not run this on the UI thread.
     */
    private void updateThumbnailsBackground(Stack<Integer> modifications, Stack<Integer> additions) {
        if (modifications == null || additions == null) return;

        for(Integer i : modifications) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
        for(Integer i : additions) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
    }

    /**
     * On a background thread, download any new thumbnails, then notify the view adapter of changes
     */
    public void refreshProfileListView(Stack<Integer> modifications, Stack<Integer> additions) {
        // Show
        activity.setTitle("Find Friends (" + foundProfiles.size() + ")");

        // Refresh on background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            updateThumbnailsBackground(modifications, additions);
            activity.runOnUiThread(() -> {
                notifyAdapter(modifications, additions);
            });
            return null;
        });
    }
}
