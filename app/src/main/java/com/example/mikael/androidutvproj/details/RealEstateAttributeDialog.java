package com.example.mikael.androidutvproj.details;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mikael.androidutvproj.EditTextNumber;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.ThousandSeparator;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * DialogFragment with input controls to set attributes to a RealEstate instance<br>
 *     a new RealEstate is initiated to this dialog<br>
 *     to set a specific RealEstate, use {@link #newInstance(RealEstate)}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class RealEstateAttributeDialog extends DialogFragment {


    private static final String BUNDLEKEY_REALESTATE = "REALESTATE";

    /**
     * this current RealEstate that will be edited
     */
    private RealEstate mRealEstate;

    /**
     * this DialogButtonListener
     */
    private DialogButtonListener mDialogButtonListener = new DialogButtonListener();


    public RealEstateAttributeDialog() {
        this.mRealEstate = new RealEstate();
    }

    public static RealEstateAttributeDialog newInstance(RealEstate realEstate){
        RealEstateAttributeDialog dfrag = new RealEstateAttributeDialog();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLEKEY_REALESTATE, realEstate);
        dfrag.setArguments(args);
        return dfrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getShowsDialog()){
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        View rootView = inflater.inflate(R.layout.layout_edit_attrs_realestate, container, false);
        init(rootView);

        return rootView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_edit_attrs_realestate, null);
        init(rootView);

        final AlertDialog AD =  new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.realestate_title))
                .setView(rootView)
                .setPositiveButton(getString(R.string.btn_ok), null)
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .create();
        AD.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDialogButtonListener.onPositiveClick()) {
                            AD.dismiss();
                        }
                    }
                });
                AD.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDialogButtonListener.onNegativeClick()) {
                            AD.dismiss();
                        }
                    }
                });
            }
        });

        return AD;
    }

    private void init(View rootView){
        initInputs(rootView);
        initContent(rootView);
    }

    private void initInputs(View v){

        // ATTR: TYPE
        ///////////////////
        Spinner edit_type = (Spinner) v.findViewById(R.id.edit_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(RealEstate.Type type : RealEstate.Type.values()){
            adapter.add(getActivity().getResources().getString(type.getNameId()));
        }

        edit_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = parent.getItemAtPosition(position).toString();
                try {
                    for (RealEstate.Type t : RealEstate.Type.values()) {
                        if (getResources().getString(t.getNameId()).equals(type))
                            mRealEstate.setType(t);
                    }
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edit_type.setAdapter(adapter);

        // ATTR : CONSTRUCT YEAR
        /////////////////////////////
        final EditText edit_constructYear = (EditText) v.findViewById(R.id.edit_constructYear);
        edit_constructYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(getActivity());
                numberPicker.setMinValue(1850);
                numberPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
                numberPicker.setValue(numberPicker.getMaxValue());

                if (mRealEstate.getConstructYear() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(mRealEstate.getConstructYear());
                    numberPicker.setValue(cal.get(Calendar.YEAR));
                }

                final Date rollbackValue =  mRealEstate.getConstructYear();
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        final Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, newVal);
                        mRealEstate.setConstructYear(cal.getTime());
                    }
                });

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.realestate_constructYear_label))
                        .setView(numberPicker)
                        .setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edit_constructYear.setText(mRealEstate.getConstructYearString());
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRealEstate.setConstructYear(rollbackValue);
                            }
                        })
                        .create().show();
            }
        });

        // ATTR : FLOOR
        /////////////////////////////
        final EditText edit_floor = (EditText) v.findViewById(R.id.edit_floor);
        edit_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listmax    = getResources().getInteger(R.integer.realestate_floor_max);
                int listmin     = getResources().getInteger(R.integer.realestate_floor_min);
                listmax -= listmin;
                listmax *= 2;
                final String[] listvals  = new String[listmax];

                for (int i = 0; i < listmax; i++){
                    double floorVal = (listmin + i) * 0.5;
                    if(floorVal % 1 == 0)
                        listvals[i] = "" + (int) floorVal;
                    else
                        listvals[i] = ""+ floorVal;
                }

                dialog_list(getString(R.string.realestate_floor_label), listvals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_floor.setText(listvals[which]);
                        mRealEstate.setFloor(Double.parseDouble(listvals[which]));
                    }
                }).create().show();
            }
        });

        // ATTR : ROOMS
        /////////////////////////////
        final EditText edit_rooms = (EditText) v.findViewById(R.id.edit_rooms);
        edit_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listmax    = getResources().getInteger(R.integer.realestate_rooms_max);
                int listmin     = getResources().getInteger(R.integer.realestate_rooms_min);
                listmax -= listmin;
                listmax *= 2;
                final String[] listvals = new String[listmax];
                double interval = 0.5;

                for (int i = 0; i < listmax; i++){
                    double roomsVal = (listmin + i) * interval;
                    if(roomsVal % 1 == 0)
                        listvals[i] = ""+ (int) roomsVal;
                    else
                        listvals[i] = ""+ roomsVal;

                }

                dialog_list(getString(R.string.realestate_rooms_label), listvals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_rooms.setText(listvals[which]);
                        mRealEstate.setRooms(Double.parseDouble(listvals[which]));

                    }
                }).create().show();
            }
        });
        // ATTR : LIVINGSPACE
        /////////////////////////////
        final EditTextNumber edit_livingspace = (EditTextNumber) v.findViewById(R.id.edit_livingspace);
        edit_livingspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listmax     = getResources().getInteger(R.integer.realestate_livingspace_max);
                int listmin     = getResources().getInteger(R.integer.realestate_livingspace_min);
                listmax -= listmin;
                listmax *= 2;

                final String[] listvals         = new String[listmax];
                double interval = 0.5;

                final String THOUSAND_SEP = getString(R.string.thousand_sep);

                for (int i = 0; i < listmax; i++){
                    double livingspaceVal = (listmin + i) * interval;
                    String strval = "";
                    if(livingspaceVal % 1 == 0)
                        strval += (int) livingspaceVal;
                    else
                        strval += livingspaceVal;
                    listvals[i] = ThousandSeparator.format("" + strval, THOUSAND_SEP);
                }

                final String unit   = getString(R.string.realestate_livingSpace_unit);
                final String title  = getString(R.string.realestate_livingSpace_label) + " (" + unit +")";
                dialog_list(title, listvals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String listval  = listvals[which] + " " + unit;
                        Log.d("reattrd", "set listval : " + listval);
                        edit_livingspace.setText(listval);

                        double dLsitVal = Double.parseDouble(listvals[which]);
                        mRealEstate.setLivingSpace(dLsitVal);
                    }
                }).create().show();
            }
        });

    }

    private void initContent(View rootView){

        if (getArguments() != null){
            Bundle args = getArguments();
            mRealEstate = args.getParcelable(BUNDLEKEY_REALESTATE);

        } else {
            mRealEstate = new RealEstate();
        }

        display(rootView, mRealEstate);
    }

    /**
     * get this current RealEstate
     * @return current RealEstates
     */
    public RealEstate getRealEstate(){

        String address      = getEditText(R.id.edit_address)    .getText().toString();
        String startbid     = getRawNumberContent(R.id.edit_startBid);
        String rent         = getRawNumberContent(R.id.edit_rent);
        String description  = getEditText(R.id.edit_description).getText().toString();

        mRealEstate.setAddress(                         address);
        mRealEstate.setStartBid(Integer.parseInt(       startbid));
        mRealEstate.setRent(Integer.parseInt(           rent));
        mRealEstate.setDescription(                     description);

        mRealEstate.setLatLng(getLocationFromAddress(address));

        return mRealEstate;
    }

    private EditText getEditText(int viewId){

        if (getShowsDialog()){
            return (EditText) getDialog().findViewById(viewId);

        } else {
            return (EditText) getView().findViewById(viewId);
        }

    }


    private void setViewText(View rootView, int viewId, int textContent){
        String strContent = Integer.toString(textContent);
        if(textContent > 1)
            setViewText(rootView, viewId, strContent);
        else
            setViewHint(rootView, viewId, strContent);
    }
    private void setViewText(View rootView, int viewId, double textContent){
        String strContent = Double.toString(textContent);
        if(textContent > 1)
            setViewText(rootView, viewId, strContent);
        else
            setViewHint(rootView, viewId, strContent);
    }

    private <TextContent> void setViewText(View rootView, int viewId, TextContent textContent){
        if (textContent == null) return;
        ((TextView)rootView.findViewById(viewId)).setText(textContent.toString());
    }

    private <TextContent> void setViewHint(View rootView, int viewId, TextContent textContent){
        if (textContent == null) return;
        ((TextView)rootView.findViewById(viewId)).setHint(textContent.toString());
    }

    private void display(View rootView, RealEstate re){
        if (re == null) return;

        setViewText(rootView, R.id.edit_address,        re.getAddress());
        setViewText(rootView, R.id.edit_constructYear,  re.getConstructYearString());
        setViewText(rootView, R.id.edit_startBid,       re.getStartBid());
        setViewText(rootView, R.id.edit_rent,           re.getRent());
        setViewText(rootView, R.id.edit_floor,          re.getFloor());
        setViewText(rootView, R.id.edit_rooms,          re.getRooms());
        setViewText(rootView, R.id.edit_livingspace,    re.getLivingSpace());
        setViewText(rootView, R.id.edit_description,    re.getDescription());
    }

    /**
     * TODO move to Dialog.java ?
     * create listitem createDialog<br>
     * @param title
     * @param listvals
     * @param clickListener
     * @param <ListValue> type of listvalue
     * @return
     */
    protected <ListValue> AlertDialog.Builder dialog_list(String title, ListValue[] listvals, DialogInterface.OnClickListener clickListener){
        List<String> menulist = new ArrayList<>();
        for(ListValue val : listvals){
            menulist.add(val.toString());
        }

        LinearLayout layout = new LinearLayout(getActivity());
        for(String option : menulist){
            TextView optionitem = new TextView(getActivity());
            optionitem.setText(option);
            layout.addView(optionitem);
        }
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, menulist);
        builder
                .setTitle(title)
                .setAdapter(adapter, clickListener)
                .setPositiveButton(getResources().getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mDialogButtonListener != null) {
                            mDialogButtonListener.onPositiveClick();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel), null);

        return builder;
    }

    /**
     * get raw text content from EditText without formatting
     * param viewId id of view to access
     * @return
     */
    private String getRawNumberContent(int viewId){
        TextView tv = null;
        if (getShowsDialog()){
            tv = (TextView) getDialog().findViewById(viewId);

        } else {
            tv = (TextView) getView().findViewById(viewId);
        }

        return tv.getText().toString().replaceAll("[^0-9]", "");
    }

    /**
     * get location LatLng position from address
     * @param strAddress
     * @return
     */
    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address == null || address.isEmpty()) {
                return null;
            }

            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            return new LatLng(location.getLatitude(), location.getLongitude());

        } catch(IOException e){

        }
        return null;
    }


    /**
     * sets this clicklistener used by Dialog
     * @param dialogButtonListener
     * @return
     */
    public RealEstateAttributeDialog setOnClickListener(DialogButtonListener dialogButtonListener){
        mDialogButtonListener = dialogButtonListener;
        return this;
    }

}
