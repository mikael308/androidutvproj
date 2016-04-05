package com.example.mikael.androidutvproj.eventlist;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.event.Apartment;
import com.example.mikael.androidutvproj.event.Event;
import com.example.mikael.androidutvproj.event.Photo;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;

/**
 *
 * displays a com.example.mikael.androidutvproj.event.Event<br>
 * fragment calls calling class on menu options
 *
 * @author Mikael Holmbom
 * @version 1.1
 *
 * @see com.example.mikael.androidutvproj.event.Event
 * @see EventListener
 *
 */
public class EventFragment extends Fragment {

    /**
     * activity using this fragment
     */
    protected EventListener mEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if (context instanceof Activity)
                mEventListener = (EventListener) context;

        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + EventListener.class);
        }
    }


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
        inflater.inflate(R.menu.menu_event, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void displayEmpty(){
        displayEvent(new Event(""));
    }
    /**
     * display event in this fragment view
     * @param event event to display
     */
    public void displayEvent(Event event) {
        if(event == null) return;

        mCurrentEvent = event;
        Apartment apartm = event.getApartment();
        Activity act = getActivity();
        ((TextView) act.findViewById(R.id.apartment_address)).setText(apartm.getAddress());
        String type = getResources().getString(apartm.getType().getNameId());
        ((TextView) act.findViewById(R.id.apartment_type)).setText(type);
        ((TextView) act.findViewById(R.id.apartment_constructYear)).setText(apartm.getConstructYearString());
        ((TextView) act.findViewById(R.id.apartment_openHouseDate)).setText(event.getDateString(Event.DATEFORMAT_STDFORMAT));

        TextView startbid = ((TextView) act.findViewById(R.id.apartment_startBid));
        startbid.addTextChangedListener(new MyWatcher());

        ((TextView) act.findViewById(R.id.apartment_startBid)).setText(String.valueOf(apartm.getStartBid()));
        ((TextView) act.findViewById(R.id.apartment_rent)).setText(String.valueOf(apartm.getRent()));
        ((TextView) act.findViewById(R.id.apartment_floor)).setText(String.valueOf(apartm.getFloor()));
        ((TextView) act.findViewById(R.id.apartment_rooms)).setText(String.valueOf(apartm.getRooms()));
        ((TextView) act.findViewById(R.id.apartment_livingSpace)).setText(String.valueOf(apartm.getLivingSpace()));

        double priceLivingSpace = apartm.getLivingSpace() == 0? 0: apartm.getStartBid() / apartm.getLivingSpace();
        ((TextView) act.findViewById(R.id.apartment_priceLivingSpace)).setText(String.valueOf(priceLivingSpace));

        ((TextView) act.findViewById(R.id.apartment_description)).setText(apartm.getDescription());
    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == getActivity().RESULT_OK){

                savePhotoDialog().show();

            } else if (resultCode == getActivity().RESULT_CANCELED){
                //TODO USER CANCELED
                Log.d("hal", "RESULT_CANCEL");

            }
        }

        if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE){
            if(resultCode == getActivity().RESULT_OK){
                Log.d("hal", "datastring : " + data.getDataString());
                Log.d("hal", "data       : " + data.getData());
            } else if(resultCode == getActivity().RESULT_CANCELED){
                Log.d("hal", "cancel");
                //TODO USER CANCELED
            } else {
                Log.d("hal", "error");
                //TODO ERROR SHOW TOAST ??
            }
        }
    }
    */


    public AlertDialog dialog_editEvent(Event event){

        final View v = getActivity().getLayoutInflater().inflate(R.layout.layout_apartment_edit, null);

        // add to event to this dialog
        Apartment a = event.getApartment();
        ((EditText)v.findViewById(R.id.edit_address))      .setText(a.getAddress());
        ((EditText)v.findViewById(R.id.edit_type))         .setText(a.getType().name());
        ((EditText)v.findViewById(R.id.edit_constructyear)).setText(a.getConstructYearString());
        ((EditText)v.findViewById(R.id.edit_openHouseDate)).setText(event.getDateString(Event.DATEFORMAT_STDFORMAT));
        ((EditText)v.findViewById(R.id.edit_startBid))     .setText(String.valueOf(a.getStartBid()));
        ((EditText)v.findViewById(R.id.edit_rent))         .setText(String.valueOf(a.getRent()));
        ((EditText)v.findViewById(R.id.edit_floor))        .setText(String.valueOf(a.getFloor()));
        ((EditText)v.findViewById(R.id.edit_rooms))        .setText(String.valueOf(a.getRooms()));
        ((EditText)v.findViewById(R.id.edit_livingspace))  .setText(String.valueOf(a.getLivingSpace()));
        ((EditText)v.findViewById(R.id.edit_description))  .setText(a.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Event rollbackEvent = event;

        builder.setTitle(getResources().getString(R.string.event_entry_dialog_edit_title))
                .setView(v)
                .setIcon(R.drawable.ic_mode_edit_black_24dp)
                .setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO edit in database

                        String address = ((EditText) v.findViewById(R.id.edit_address)).getText().toString();
                        String type = ((EditText) v.findViewById(R.id.edit_type)).getText().toString();
                        String constructYear = ((EditText) v.findViewById(R.id.edit_constructyear)).getText().toString();
                        String openHouse = ((EditText) v.findViewById(R.id.edit_openHouseDate)).getText().toString();
                        String startBid = ((EditText) v.findViewById(R.id.edit_startBid)).getText().toString();
                        String rent = ((EditText) v.findViewById(R.id.edit_rent)).getText().toString();
                        String floor = ((EditText) v.findViewById(R.id.edit_floor)).getText().toString();
                        String rooms = ((EditText) v.findViewById(R.id.edit_rooms)).getText().toString();
                        String livingspace = ((EditText) v.findViewById(R.id.edit_livingspace)).getText().toString();
                        String description = ((EditText) v.findViewById(R.id.edit_description)).getText().toString();

                        try {
                            Event newEvent = new Event(address);
                            //TODO fix openhouse
                            newEvent.setDate(new Date());

                            Apartment a = newEvent.getApartment();

                            a.setType(Apartment.Type.CONDOMINIUM); //TODO tmp
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
                        String photoDescr = ((EditText) VIEW.findViewById(R.id.edit_openHouseDate)).getText().toString();
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
