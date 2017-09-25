package com.ots.tdd.onthespectrum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.app.ActionBar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Show the action bar and return arrow at the top of the screen
        // TODO: Fix this.
        // Potential fix: https://stackoverflow.com/questions/24596494/android-app-crashes-on-startup-in-emulator
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
    }

    public void moveToCallScreen(View v) {
        Intent intentCall = new Intent(this, CallActivity.class);
        startActivity(intentCall);
    }

    public void moveToProfileScreen(View v) {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
    }

    public void moveToSettingsScreen(View v) {
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    public void moveToEmergencyListScreen(View v) {
        Intent intentEmergencyList = new Intent(this, EmergencyListActivity.class);
        startActivity(intentEmergencyList);
    }

    public void moveToTestVoiceScreen(View v) {
        Intent intentTestVoice = new Intent(this, TestVoiceActivity.class);
        startActivity(intentTestVoice);
    }
}
