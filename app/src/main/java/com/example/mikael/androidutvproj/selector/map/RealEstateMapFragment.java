package com.example.mikael.androidutvproj.selector.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.mikael.androidutvproj.Observer;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.settings.Permission;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * map containing markers of RealEstate objects<br>
 * if calling activity implements OnMarkerClickListener, its listener will be called first, if return false RealEstateMapFragment OnMarkerClickListener will be called
 * @author Mikael Holmbom
 * @version 1.0
 * @see RealEstate
 */
public class RealEstateMapFragment extends MapFragment
        implements GoogleMap.OnMarkerClickListener, Observer{

    /**
     * drawable resource used for markers
     */
    private static final int marker_ic = R.drawable.ic_home_black_24dp;

    /**
     * this fragments map
     */
    private GoogleMap mMap;
    /**
     * this maps markerbounds
     */
    private LatLngBounds mMarkerBounds;

    /**
     * LatLng : the LatLng of RealEstate
     * RealEstate :
     */
    private Map<RealEstate, Marker> mCurrentItems = new HashMap<>();

    /**
     * Activity using this mapfragment
     */
    private GoogleMap.OnMarkerClickListener mCallingOnMarkerClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            if (context instanceof Activity)
                mCallingOnMarkerClickListener = (GoogleMap.OnMarkerClickListener) context;

        } catch(ClassCastException e){
            //TODO ta bort throw new ClassCastException(context.toString() + " must implement " + GoogleMap.OnMarkerClickListener.class);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            final MapFragment mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_selector_map);
            mapfragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setOnMarkerClickListener(RealEstateMapFragment.this);
                    mMap.getUiSettings().setMapToolbarEnabled(true);

                    init_Map();
                    init_buttons();
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mMap = mapfragment.getMap();

                }
            }).start();



        }

        ////////////////////////////////////////////////////////////////////////////////////////////



/*TODO denna funkar....
        if (mMap == null) {
            MapFragment mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_selector_map);
            mapfragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setOnMarkerClickListener(RealEstateMapFragment.this);
                    mMap.getUiSettings().setMapToolbarEnabled(true);

                    init_Map();

                    init_buttons();
                }
            });

            mMap = mapfragment.getMap();
        }
*/

    }

    private void init_buttons(){

        ImageButton btn_showAll = (ImageButton) getActivity().findViewById(R.id.btn_showAll);
        btn_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCameraPos(DataMapper.getCurrentRealEstate().getLatLng());
            }
        });
        btn_showAll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAllItems();
                return true;
            }
        });

    }


    public void init_Map() {

        if(Permission.askPermissionIfNeeded(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)){

            Log.d("hal", "init_map : permissions OK");

            mMap.setPadding(0, 0, 0, 0);

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){
                public void onMapLoaded(){
                    showAllItems();
                }
            });

        }
    }


    /**
     * set map view to show all current items<br>
     *     if no current items: do nothing
     */
    public void showAllItems(){
        if(mMarkerBounds == null
                || mCurrentItems.isEmpty()
                || ! this.isVisible()) return;

        updateCameraPos(mMarkerBounds);
    }

    private void updateCameraPos(LatLng latLng){
        if(latLng == null) return;

        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(update);
    }

    private void updateCameraPos(LatLngBounds latLngBounds){
        if (latLngBounds == null) return;

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(latLngBounds, getResources().getInteger(R.integer.map_latlngbounds_padding));
        mMap.animateCamera(update);
    }

    /**
     * calls calling activity if it implements OnMarkerClickListener, if this listener return false this method is called:<br>
     *     shows marker info
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        if(mCallingOnMarkerClickListener != null
                && mCallingOnMarkerClickListener.onMarkerClick(marker)){
            return true;
        }

        marker.showInfoWindow();

        return true;
    }

    public void clickOnItem(RealEstate realEstate){
        Marker marker = mCurrentItems.get(realEstate);
        marker.showInfoWindow();

    }

    /**
     * adds marker from realEstate to current map<br>
     * updates this maps items bounds
     * @param realEstate
     */
    public boolean addMarker(RealEstate realEstate){
        if(realEstate == null || realEstate.getLatLng() == null)
            return false;

        Marker marker = mMap.addMarker(new MarkerOptions()
                .title(realEstate.getAddress())
                .icon(BitmapDescriptorFactory.fromResource(marker_ic))
                .position(realEstate.getLatLng()));

        //TODO tmp dbg
        Log.d("mapfrag", "adding marker for " + realEstate.getLabel());
        Log.d("mapfrag", "pos : " + realEstate.getLatLng().latitude + "," + realEstate.getLatLng().longitude);


        mCurrentItems.put(realEstate, marker);
        updateBounds(realEstate);
        return true;
    }

    /**
     * adds marker from realEstate to current map
     * @param realEstate
     */
    public void addMarkerAndFocus(RealEstate realEstate){
        addMarker(realEstate);
        updateCameraPos(realEstate.getLatLng());
    }

    public void deleteMarker(RealEstate realEstate){
        if (realEstate == null) return;

        Marker marker = mCurrentItems.get(realEstate);
        if(marker != null)
            marker.remove();

        mCurrentItems.remove(realEstate);
        updateBounds();
    }

    /**
     * updateResetView current {@link #mMarkerBounds} with current {@link #mCurrentItems} positions<br>
     *     if {@link #mCurrentItems} is empty, {@link #mMarkerBounds} will be set to null
     */
    private void updateBounds(){
        mMarkerBounds = null;

        Iterator<Marker> it = mCurrentItems.values().iterator();
        if (it.hasNext()) {
            Marker firstMarker = it.next();
            mMarkerBounds = new LatLngBounds(firstMarker.getPosition(), firstMarker.getPosition());

            while(it.hasNext()){
                Marker marker = it.next();
                updateBounds(marker.getPosition());
            }
        }
    }

    /**
     * set current {@link #mMarkerBounds} to include new latLng
     * @param latLng
     */
    private void updateBounds(LatLng latLng){
       if(mMarkerBounds == null){
           mMarkerBounds = new LatLngBounds(latLng, latLng);
       }
       mMarkerBounds = mMarkerBounds.including(latLng);
    }

    /**
     * updateResetView current bounds to include realEstates position
     * @param realEstate
     */
    private void updateBounds(RealEstate realEstate){
        updateBounds(realEstate.getLatLng());
    }



    @Override
    public String getObserverId() {
        return getClass().toString();
    }

    @Override
    public void updateData() {
        Log.d("remapfrag","updateData" );
    }

    @Override
    public void updateResetView() {
        //TODO init??
        Log.d("remapfrag", "updateResetView");
        //updateCameraPos(mMarkerBounds);
    }

    @Override
    public void updateAdd(DataAccessObject item) {
        Log.d("remapfrag", "updateAdd");
        RealEstate re = (RealEstate) item;
        addMarkerAndFocus(re);

    }

    @Override
    public void updateDelete(DataAccessObject item) {
        Log.d("remapfrag", "updateDelete");
        RealEstate re = (RealEstate) item;
        deleteMarker(re);
    }
}
