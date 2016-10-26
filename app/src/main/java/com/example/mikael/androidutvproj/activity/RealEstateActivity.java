package com.example.mikael.androidutvproj.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.example.mikael.androidutvproj.settings.Settings;
import com.example.mikael.androidutvproj.tool.PostponedUITask;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.tool.RandomString;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.database.DatabaseDialog;
import com.example.mikael.androidutvproj.details.RealEstateDetailsFragment;
import com.example.mikael.androidutvproj.selector.list.RealEstateListAdapter;
import com.example.mikael.androidutvproj.selector.list.RealEstateListFragment;
import com.example.mikael.androidutvproj.selector.map.RealEstateMapFragment;
import com.example.mikael.androidutvproj.settings.Permission;
import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * activity showing RealEstates
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class RealEstateActivity extends AppCompatActivity
        implements RealEstateListFragment.RealEstateListListener, GoogleMap.OnMarkerClickListener{


    /**
     * key used in saveInstanceState/restoreInstanceState : holds value of current used RealEstate
     */
    private static String BUNDLEKEY_CURRENTREALESTATE_IDX = "BUNDLEKEY_CURRENTREALESTATE";

    private final static String BUNDLEKEY_SELECTOR_LIST_VISIBLE = "SELECTOR";

    private String BUNDLEKEY_LANGCODE = "LANGCODE";

    /**
     * holds value of the language displayed in this view from resources
     */
    private String mActLang = "";

    private boolean mListVisible = true;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * determine if current orientation is landscape
     * @return true if current orientation is landscape
     */
    private boolean isLandscapeOrientation(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("react", "onCreate(Bundle)");

        setContentView(R.layout.activity_realestate);

        initEntries();
        initButtons();

        DataMapper.setObservers(
                getListFragment(),
                getDisplayFragment(),
                getMapFragment());

    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        String settingsLangCode = Settings.getCurrentLangCode();

        if(! settingsLangCode.equalsIgnoreCase(mActLang)){
            mActLang = settingsLangCode;
            recreate();
        }*/
    }

    private List<RealEstate> getRealEstateList(){
        return DataMapper.getRealEstateList();
    }

    public RealEstate getCurrentRealEstate(){
        return DataMapper.getCurrentRealEstate();
    }


    /**
     * read entries from database
     */
    private void initEntries(){
        new PostponedUITask(this, DataMapper.init(this)){
            @Override
            public void runPostponedUITask() {
                initSelectors(getRealEstateList());
                initDetails(getCurrentRealEstate());
            }
        }.start();

    }

    private void initSelectors(List<RealEstate> realEstateList){
        for(RealEstate re : realEstateList){
            RealEstateMapFragment mapf = getMapFragment();
            if(mapf != null)
                mapf.addMarker(re);
        }
    }

    private void initDetails(RealEstate realEstate){
        if(isShowingDetails())
            getDisplayFragment().setView(realEstate);
    }

    private void initButtons(){
        //TODO borde kanske dessa istället finnas i realestatelistfragment? likaså switcher under
        ImageButton btn_add = (ImageButton) findViewById(R.id.btn_add);

        final Activity activity = this;
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewRealEstate();
            }
        });
        btn_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO debug
                DatabaseDialog.newRealEstate(activity, getRandomRealEstate())
                    .show(getSupportFragmentManager(), "newRealEstateRnd");

                return true;
            }
        });

        final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        ToggleButton btn_toggle = (ToggleButton) findViewById(R.id.toggleButton);
        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                mListVisible = !mListVisible;
            }
        });

    }


    private RealEstateMapFragment getMapFragment(){
        return (RealEstateMapFragment) getFragmentManager().findFragmentById(R.id.fragment_selector_map);
    }

    private RealEstateListFragment getListFragment(){
        return (RealEstateListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_selector_list);
    }

    private RealEstateListAdapter getListAdapter(){
        return (RealEstateListAdapter) getListFragment().getListAdapter();
    }

    private RealEstateDetailsFragment getDisplayFragment(){
        return (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_details_view);
    }

    /**
     * sets current RealEstate and displays details
     * @param realEstate
     */
    private void setCurrentRealEstate(RealEstate realEstate){
        DataMapper.setCurrentRealEstate(realEstate);

        Log.d("react", "setCurrent(" + realEstate.getLabel()+ ")");
        if (isShowingDetails()){
            getDisplayFragment().setViewAnimated(realEstate);

        } else {
            Intent intent = new Intent();
            intent.setClass(this, RealEstateDetailActivity.class);

            startActivity(intent);
        }
    }



    private boolean isShowingDetails(){
        RealEstateDetailsFragment dfrag = getDisplayFragment();
        return dfrag != null && dfrag.isVisible();
    }

    //TODO DEBUG ta bort denna, tmp method
    private RealEstate getRandomRealEstate(){
        Random rand = new Random();

        String apId = RandomString.getRandomAlphaString(rand.nextInt(9) + 1);
        String address = apId;
        address += "street " + rand.nextInt(100)+1;

        RealEstate re = new RealEstate("realestate-"+apId);
        re.setAddress(address);
        re.setType(RealEstate.Type.newType(rand.nextInt(6)));
        re.setStartBid(100_000 + rand.nextInt(2_000_000));
        re.setLivingSpace(10 + rand.nextInt(90));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1950 + rand.nextInt(66));
        Date constrYear = cal.getTime();

        double lat = -100      + rand.nextInt(100);
        double lng = 0    + rand.nextInt(100);



        re.setLatLng(new LatLng(lat, lng));
        Log.d("mapfrag", "creating with pos : " + re.getLatLng().latitude + "," + re.getLatLng().longitude);

        re.setConstructYear(constrYear);

        re.setFloor((1 + rand.nextInt(7)) * 0.5);
        re.setRent(500 + rand.nextInt(3_000));
        String descr = "here at " + address + " everything is fake, but its a great view.";
        if (rand.nextBoolean())
            descr += " or in other words, " + RandomString.getRandomAlphaString(rand.nextInt(150) + 20);

        re.setDescription(descr);
        re.setRooms(rand.nextInt(20) * 0.5);

        return re;
    }


    /**
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLEKEY_CURRENTREALESTATE_IDX, DataMapper.getCurrentRealEstate());

        Log.d("react", "SAVE list visble ? " + mListVisible);
        outState.putBoolean(BUNDLEKEY_SELECTOR_LIST_VISIBLE, mListVisible);

        //outState.putString(BUNDLEKEY_LANGCODE, mActLang);
    }
    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mListVisible = savedInstanceState.getBoolean(BUNDLEKEY_SELECTOR_LIST_VISIBLE);
        ToggleButton selectorswitch_btn = (ToggleButton) findViewById(R.id.toggleButton);

        Log.d("react", "RESTORE list visble ? " + mListVisible);

        if(! mListVisible){

            selectorswitch_btn.callOnClick();
            //switcher.showNext();
            //mListVisible = !mListVisible;

        }

        //mActLang = savedInstanceState.getString(BUNDLEKEY_LANGCODE);
    }


    ///////////////////////
    // DISPLAYLISTENER
    //
    // see RealEstateDetailsFragment
    /////////////////////

    @Override
    public void onNewRealEstate() {
        DatabaseDialog.newRealEstate(this)
        .show(getSupportFragmentManager(), "newRealEstate");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final int GRANTED = PackageManager.PERMISSION_GRANTED;

        switch(requestCode){
            case Permission.ASK_PERMISSION_REQUEST_CODE:
                for(int i = 0; i < permissions.length; i++){
                    if (grantResults[i] != GRANTED) continue;

                    switch(permissions[i]){
                        case Manifest.permission.ACCESS_COARSE_LOCATION:
                            getMapFragment().init_Map();
                            break;
                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            getMapFragment().init_Map();
                            break;
                    }
                }

                break;
        }
    }


    //////////////////////
    // SELECTOR FRAGMENT LISTENER
    //
    // see RealEstateList :  OnListItemClickListener
    //     RealEstateMapFragment :  OnMarkerClickListener
    /////////////////////

    @Override
    public void onItemClick(RealEstate selectedItem, int position) {
        try{
            setCurrentRealEstate(selectedItem);

        } catch(IndexOutOfBoundsException e){
            Log.e("react", e.getMessage());
        }

    }

    @Override
    public void onItemLongClick(RealEstate selectedItem, int position) {
        try{
            DatabaseDialog.realEstateListitem(this, selectedItem).show();

        } catch(IndexOutOfBoundsException e){
            Log.e("react", e.getMessage());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for(RealEstate realEstate : getRealEstateList()){
            if(realEstate.getLatLng().equals(marker.getPosition())){
                setCurrentRealEstate(realEstate);
                return true;
            }
        }
        return false;
    }

}
