package com.example.mikael.androidutvproj.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.settings.Settings;

/**
 * main activity : Launcher
 *
 * @author Mikael Holmbom
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Settings.initSettings(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_buttons();
    }
    /**
     * initialize this buttons
     */
    private void init_buttons(){

        //////////////////////////
        // EVENTS
        /////////////////////////
        Button btn_realestates     = (Button) findViewById(R.id.menu_btn_events);
        btn_realestates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RealEstateActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_settings:  startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_about:     createAboutDialog().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * create dialog showing information about this app<br>
     * @return aboutdialog
     */
    private AlertDialog createAboutDialog(){

        View aboutmessage = getLayoutInflater().inflate(R.layout.layout_aboutmessage, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder .setTitle(getResources().getString(R.string.about_title))
                .setIcon(R.drawable.ic_help_outline_black_24dp)
                .setView(aboutmessage)
                .setPositiveButton(getResources().getString(R.string.btn_ok), null);

        return builder.create();
    }


}
