package com.example.mikael.androidutvproj.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.event.Apartment;
import com.example.mikael.androidutvproj.event.Event;
import com.example.mikael.androidutvproj.event.Photo;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * displays a com.example.mikael.androidutvproj.event.Event
 *
 * @author Mikael Holmbom
 * @version 1.0
 *
 * @see com.example.mikael.androidutvproj.event.Event
 *
 */
public class EventFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    /**
     * this current displayed event
     */
    private Event mCurrentEvent;


    private class MyWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable view) {
            String s = null;
            try {
                // The comma in the format specifier does the trick
                s = String.format("%,d", Long.parseLong(view.toString()));
            } catch (NumberFormatException e) {
            }
            // Set s back to the view after temporarily removing the text change listener
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true); // add callback to actionbar menu
        View rootView = inflater.inflate(R.layout.layout_event, container, false);

        ////////////////////////////
        // NEW PHOTO BUTTON
        //////////////////////////
        ImageButton btnStartCamera = (ImageButton) rootView.findViewById(R.id.btn_startcamera);
        btnStartCamera.setPadding(30, 30, 30, 30);
        btnStartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imgFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());

                File imgFile = new File(imgFolder, "img_001.jpg"); //TODO fix correct imgfile names.. format: address_001.jpg

                try {
                    if (imgFile.createNewFile()) {
                        Uri imgUri = Uri.fromFile(imgFile);
                        Log.d("hal", "file uri : " + imgUri.getPath());

                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(intentCamera, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }

                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_edit: editEntryRequest(mCurrentEvent).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * display event in this fragment view
     * @param event event to display
     */
    public void displayEvent(Event event) {
        if(event == null) return;

        mCurrentEvent = event;
        Apartment apartment = event.getApartment();
        Activity a = getActivity();
        ((TextView) a.findViewById(R.id.apartment_address)).setText(apartment.getAddress());
        ((TextView) a.findViewById(R.id.apartment_type)).setText(apartment.getType());
        ((TextView) a.findViewById(R.id.apartment_constructYear)).setText(apartment.getConstructYearString());
        ((TextView) a.findViewById(R.id.apartment_openHouseDate)).setText(event.getDateString(Event.DATEFORMAT_STDFORMAT));

        TextView startbid = ((TextView) a.findViewById(R.id.apartment_startBid));
        startbid.addTextChangedListener(new MyWatcher());

        ((TextView) a.findViewById(R.id.apartment_startBid)).setText(String.valueOf(apartment.getStartBid()));
        ((TextView) a.findViewById(R.id.apartment_rent)).setText(String.valueOf(apartment.getRent()));
        ((TextView) a.findViewById(R.id.apartment_floor)).setText(String.valueOf(apartment.getFloor()));
        ((TextView) a.findViewById(R.id.apartment_rooms)).setText(String.valueOf(apartment.getRooms()));
        ((TextView) a.findViewById(R.id.apartment_livingSpace)).setText(String.valueOf(apartment.getLivingSpace()));

        double priceLivingSpace = apartment.getLivingSpace() == 0? 0: apartment.getStartBid() / apartment.getLivingSpace();
        ((TextView) a.findViewById(R.id.apartment_priceLivingSpace)).setText(String.valueOf(priceLivingSpace));

        ((TextView) a.findViewById(R.id.apartment_description)).setText(apartment.getDescription());
    }

    private AlertDialog editEntryRequest(Event event){

        final View v = getActivity().getLayoutInflater().inflate(R.layout.layout_eventsettings, null);

        // add to event to this dialog
        Apartment a = event.getApartment();
        ((EditText)v.findViewById(R.id.input_address))      .setText(a.getAddress());
        ((EditText)v.findViewById(R.id.input_type))         .setText(a.getType());
        ((EditText)v.findViewById(R.id.input_constructyear)).setText(a.getConstructYearString());
        ((EditText)v.findViewById(R.id.input_openHouseDate)).setText(event.getDateString(Event.DATEFORMAT_STDFORMAT));
        ((EditText)v.findViewById(R.id.input_startBid))     .setText(String.valueOf(a.getStartBid()));
        ((EditText)v.findViewById(R.id.input_rent))         .setText(String.valueOf(a.getRent()));
        ((EditText)v.findViewById(R.id.input_floor))        .setText(String.valueOf(a.getFloor()));
        ((EditText)v.findViewById(R.id.input_rooms))        .setText(String.valueOf(a.getRooms()));
        ((EditText)v.findViewById(R.id.input_livingspace))  .setText(String.valueOf(a.getLivingSpace()));
        ((EditText)v.findViewById(R.id.input_description))  .setText(a.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Event rollbackEvent = event;

        builder.setTitle(getResources().getString(R.string.event_entry_dialog_edit_title))
                .setView(v)
                .setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO edit in database

                        String address      = ((EditText) v.findViewById(R.id.input_address)).getText().toString();
                        String type         = ((EditText) v.findViewById(R.id.input_type)).getText().toString();
                        String constructYear = ((EditText) v.findViewById(R.id.input_constructyear)).getText().toString();
                        String openHouse    = ((EditText) v.findViewById(R.id.input_openHouseDate)).getText().toString();
                        String startBid     = ((EditText) v.findViewById(R.id.input_startBid)).getText().toString();
                        String rent         = ((EditText) v.findViewById(R.id.input_rent)).getText().toString();
                        String floor        = ((EditText) v.findViewById(R.id.input_floor)).getText().toString();
                        String rooms        = ((EditText) v.findViewById(R.id.input_rooms)).getText().toString();
                        String livingspace  = ((EditText) v.findViewById(R.id.input_livingspace)).getText().toString();
                        String description  = ((EditText) v.findViewById(R.id.input_description)).getText().toString();

                        try {
                            Event newEvent = new Event(address);
                            //TODO fix openhouse
                            newEvent.setDate(new Date());

                            Apartment a = newEvent.getApartment();

                            a.setType(type);
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, Integer.parseInt(constructYear));
                            a.setConstructYear(cal.getTime());
                            a.setStartBid(Integer.parseInt(startBid));
                            a.setRent(Integer.parseInt(rent));
                            a.setFloor(Double.parseDouble(floor));
                            a.setRooms(Double.parseDouble(rooms));
                            a.setLivingSpace(Double.parseDouble(livingspace));
                            a.setDescription(description);

                            displayEvent(newEvent);

                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.edit_toast_editentry), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            //TODO add fail msg
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.event_entry_dialog_newevent_toast_eventCreatedError), Toast.LENGTH_LONG).show();
                            displayEvent(rollbackEvent);
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private AlertDialog savePhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View VIEW = getActivity().getLayoutInflater().inflate(R.layout.layout_newphoto, null);

        builder.setTitle(getResources().getString(R.string.event_newphoto_dialog_title))
                .setView(VIEW)
                .setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Context context = getActivity().getApplicationContext();
                        String photoDescr = ((EditText) VIEW.findViewById(R.id.input_openHouseDate)).getText().toString();
                        if ( Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "no access valid", Toast.LENGTH_LONG);
                        }
                        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                        Photo newPhoto = new Photo(photoDescr, currentLatlng);

                        mCurrentEvent.addPhoto(newPhoto);
                        //updateGridview();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel), null);

        return builder.create();

    }
}
