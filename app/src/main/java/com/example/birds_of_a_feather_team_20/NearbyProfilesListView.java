package com.example.birds_of_a_feather_team_20;


import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Stack;

public class NearbyProfilesListView {
    private final Activity mainActivity;
    private final ProfilesViewAdapter adapter;
    private final List<Profile> foundProfiles;

    public NearbyProfilesListView(Activity mainActivity, List<Profile> foundProfiles) {
        this.mainActivity = mainActivity;
        this.foundProfiles = foundProfiles;
//        this.nearbyManager = mainActivity.getNearbyManager();

        RecyclerView basicRecycler = mainActivity.findViewById(R.id.profile_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mainActivity);
        basicRecycler.setLayoutManager(layoutManager);
        adapter = new ProfilesViewAdapter(mainActivity, foundProfiles);
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
}
