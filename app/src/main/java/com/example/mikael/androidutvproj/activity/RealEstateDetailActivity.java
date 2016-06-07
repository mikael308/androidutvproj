package com.example.mikael.androidutvproj.activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.details.RealEstateDetailsFragment;


/**
 * Activity displays RealEstateDetailsFragment<br>
 * This activity runs only in portrait orientation, finishes on create in landscape orientation
 * @author Mikael Holmbom
 * @version 1.0
 */
public class RealEstateDetailActivity extends AppCompatActivity  {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLandscapeOrientation()) {
            finish();
        }

        setContentView(R.layout.activity_realestate_details_port);

        RealEstateDetailsFragment realEstateDetailsFragment = getDetailsFragment();
        DataMapper.attach(realEstateDetailsFragment);

        RealEstate re = DataMapper.getCurrentRealEstate();
        realEstateDetailsFragment.setView(re);

    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        RealEstateDetailsFragment realEstateDetailsFragment = getDetailsFragment();

        DataMapper.attach(
                realEstateDetailsFragment
        );

        RealEstate re = DataMapper.getCurrentRealEstate();

        realEstateDetailsFragment.setView(re);
    }


    private RealEstateDetailsFragment getDetailsFragment(){
        return (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details_view);
    }

    /**
     * determine if current orientation is landscape
     * @return true if current orientation is landscape
     */
    private boolean isLandscapeOrientation(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE; 
    }

}
