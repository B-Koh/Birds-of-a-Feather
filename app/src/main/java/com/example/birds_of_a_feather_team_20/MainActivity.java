package com.example.birds_of_a_feather_team_20;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.birds_of_a_feather_team_20.sorting.MatchComparator;
import com.example.birds_of_a_feather_team_20.sorting.SizeWeightComparator;
import com.example.birds_of_a_feather_team_20.sorting.SortDropdown;
import com.example.birds_of_a_feather_team_20.sorting.TimeWeightComparator;
import com.example.birds_of_a_feather_team_20.wave.WavePublisher;

import com.example.birds_of_a_feather_team_20.model.db.DBSession;
import com.example.birds_of_a_feather_team_20.model.db.SessionDao;
import com.example.birds_of_a_feather_team_20.model.db.SessionDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private NearbyManager nearbyManager;
    public static String sessionName;

    private SessionDatabase db;
    private SessionDao sessionDao;

    public NearbyManager getNearbyManager() {
        return nearbyManager;
    }
    private SortDropdown sortDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Find Friends");


        // Checks bluetooth permissions
        PermissionsManager permManager = new PermissionsManager(this);
        permManager.checkPermission();

        // Checks bluetooth if enabled
        BluetoothManager bt = new BluetoothManager(this);
        bt.initializeBluetooth();


        sortDropdown = new SortDropdown(findViewById(R.id.sort_dropdown), this);

        MyProfile.singleton(getApplicationContext()); //

        nearbyManager = new NearbyManager(this);
    }

    @Override
    protected void onStart() {
        Log.i("START", "MainActivity.onStart");

        for (String message : DebugActivity.messagesToAdd()) {
//            nearbyManager.sendFakeMessage(this, profile);
            nearbyManager.sendFakeMessage(message);
        }
        DebugActivity.messagesToAdd().clear();

        super.onStart();
        nearbyManager.refreshList();
        nearbyManager.republish();
        nearbyManager.resubscribe();

        sortDropdown.onStart(getNearbyManager());
    }

    @Override
    protected void onStop() {
        Log.i("STOP", "MainActivity.onStop");
        super.onStop();

    }

    public void onClickStartStop(View view) {
//        Utilities.toast(this,"Start/Stop");
        Button button = (Button)view;
        if (nearbyManager.getIsScanning()) {
            boolean success = nearbyManager.stopScanning();
            if (success) {
                button.setText("Start");
                stopSession();
            }
        } else {
            boolean success = nearbyManager.startScanning();
            if (success) {
                button.setText("Stop");
                newSessionSetName();
            }
        }
    }

    public void newSessionSetName(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        sessionName = formatter.format(date);

        db = SessionDatabase.singleton(this);
        sessionDao = db.sessionDao();
        DBSession dbSession = new DBSession(sessionName);
        sessionDao.insert(dbSession);
    }

    public void stopSession(){
        Intent intent = new Intent(this, StopSessionActivity.class);
        intent.putExtra("autosave_session", this.sessionName);
        this.startActivity(intent);
    }

    public void onLaunchFavoritesClicked(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }
    public void onLaunchProfileClicked(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }
    public void onLaunchDebugClicked(View view) {
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    /**
     * Handles result of User input of requested permissions
     * @param requestCode - Code unique to enabling Bluetooth permissions
     * @param permissions - Bluetooth permissions that are being checked
     * @param grantResults - Results from user
     * Cited Work: https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
     * Cited Work: https://developer.android.com/training/permissions/requesting#allow-system-manage-request-code
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsManager pm = new PermissionsManager(this);
        pm.onPermissionsResult(grantResults);
    }

    @Override
    protected void onDestroy() {
        WavePublisher.singleton(this).finalize(this);
        super.onDestroy();
    }

    public void onViewSessionsClicked(View view) {
        Intent intent = new Intent(this, ViewSessionsActivity.class);
        startActivity(intent);
    }
}