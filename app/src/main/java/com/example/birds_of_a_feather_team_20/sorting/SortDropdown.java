package com.example.birds_of_a_feather_team_20.sorting;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.birds_of_a_feather_team_20.MainActivity;
import com.example.birds_of_a_feather_team_20.NearbyManager;
import com.example.birds_of_a_feather_team_20.R;
import com.example.birds_of_a_feather_team_20.Utilities;

public class SortDropdown {
    String[] sortList = {MatchComparator.LABEL, TimeWeightComparator.LABEL, SizeWeightComparator.LABEL};

    private final Spinner spinner;

    public SortDropdown(Spinner spinner, Context context) {
        this.spinner = spinner;
        ArrayAdapter<String> sort_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sortList);
        sort_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(sort_adapter);
    }

    public void onStart(NearbyManager manager) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Sort Method", spinner.getSelectedItem().toString());
                manager.changeSort(spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
