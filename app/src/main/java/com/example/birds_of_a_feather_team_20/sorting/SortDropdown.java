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

/**
 * Class for setting up and handling events of the sorting dropdown on the find friends tab.
 * This class sets up the values in the dropdown (spinner) and when the selected value changes,
 * it updates the sorting
 */
public class SortDropdown {
    String[] sortList = {MatchComparator.LABEL, TimeWeightComparator.LABEL, SizeWeightComparator.LABEL};

    private final Spinner spinner;

    /**
     * Constructor sets up the spinner and the adapter for the spinner
     *
     * @param spinner The spinner UI view
     * @param context context
     */
    public SortDropdown(Spinner spinner, Context context) {
        this.spinner = spinner;
        ArrayAdapter<String> sort_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sortList);
        sort_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(sort_adapter);
    }

    /**
     * Call this when the activity onStart() is invoked. This sets a listener for the spinner
     * value changing, and updates the NearbyManager with the new sort type when it changes.
     *
     * @param manager The NearbyManager to update
     */
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
