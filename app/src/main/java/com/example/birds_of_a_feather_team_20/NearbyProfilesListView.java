package com.example.birds_of_a_feather_team_20;


import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;

public class NearbyProfilesListView {
    private final Activity activity;
    private final ProfilesViewAdapter adapter;
    private final List<Profile> foundProfiles;

    public NearbyProfilesListView(Activity activity) {
        this.activity = activity;
        this.foundProfiles = ProfilesCollection.singleton().getProfiles();
//        this.nearbyManager = mainActivity.getNearbyManager();

        RecyclerView basicRecycler = activity.findViewById(R.id.profile_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        basicRecycler.setLayoutManager(layoutManager);
        adapter = new ProfilesViewAdapter(activity, foundProfiles);
        basicRecycler.setAdapter(adapter);
    }


    public void updateList(Stack<Integer> modifications, Stack<Integer> additions) {

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
//        adapter.notifyDataSetChanged();
//        adapter = new ProfilesViewAdapter(NearbyManager.nearbyProfiles);
//        basicRecycler.setAdapter(adapter);
    }

    public void updateThumbnailsBackground(Stack<Integer> modifications, Stack<Integer> additions) {
//        for (Profile p : foundProfiles) {
//            if (p != null) {
//                p.getThumbnail();
//            }
//        }



        for(Integer i : modifications) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
        for(Integer i : additions) {
            if (i != null)
                foundProfiles.get(i).getThumbnail();
        }
    }


    public void refreshProfileListView(Stack<Integer> modifications, Stack<Integer> additions) {
        activity.setTitle("Find Friends (" + foundProfiles.size() + ")"); // FIXME: May not want this here

        // Refresh on background thread
        Executors.newSingleThreadExecutor().submit(() -> {
            updateThumbnailsBackground(modifications, additions);
            activity.runOnUiThread(() -> {
                updateList(modifications, additions);
            });
            return null;
        });
    }
}
