package com.example.mikael.androidutvproj.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.settings.SettingsListFragment;

/**
 * activity used as settings for this app
 * @author Mikael Holmbom
 * @version 1.0
 * @see SettingsListFragment
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        
    }
}
