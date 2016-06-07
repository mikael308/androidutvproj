package com.example.mikael.androidutvproj.details;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikael.androidutvproj.PhotoFile;
import com.example.mikael.androidutvproj.settings.Permission;
import com.example.mikael.androidutvproj.tool.CameraTool;
import com.example.mikael.androidutvproj.tool.PostponedUITask2;
import com.example.mikael.androidutvproj.activity.PhotoDetailActivity;
import com.example.mikael.androidutvproj.activity.SettingsActivity;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.Dialog;
import com.example.mikael.androidutvproj.Observer;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.anim.Animators;
import com.example.mikael.androidutvproj.dao.Photo;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.dao.Event;
import com.example.mikael.androidutvproj.tool.PostponedUITask;
import com.example.mikael.androidutvproj.database.DatabaseDialog;
import com.example.mikael.androidutvproj.settings.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * displays a com.example.mikael.androidutvproj.event.RealEstate<br>
 * fragment calls calling class on menu options
 *
 * @author Mikael Holmbom
 * @version 1.1
 *
 * @see RealEstate
 *
 */
public class RealEstateDetailsFragment extends Fragment implements Observer {


    /**
     * path to last photo taken by this app session
     */
    private String mPhotoPath;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.layout_details_realestate, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setView(DataMapper.getCurrentRealEstate());
        initButtons();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void initButtons(){
        View root;
        try {
            root = getView();
        } catch (NullPointerException e){
            Log.e("Error", e.getMessage());
            return;
        }

        ImageButton btn_startCamera = (ImageButton) root.findViewById(R.id.btn_startcamera);
        btn_startCamera.setPadding(30, 30, 30, 30);
        btn_startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("redetailfrag", "click on camera");
                startCamera();
            }
        });


        ImageButton btn_showing = (ImageButton) root.findViewById(R.id.btn_showing);
        btn_showing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowingsMenu();
            }
        });

        ImageButton btn_delete = (ImageButton) root.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteCurrent();
            }
        });

        ImageButton btn_edit = (ImageButton) root.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditCurrent();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_realestate_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_new:
                DatabaseDialog.newRealEstate(getActivity())
                        .show(getActivity().getSupportFragmentManager(), "newRealEstate");
                break;
            case R.id.action_edit:
                onEditCurrent();
               break;
            case R.id.action_delete:
                onDeleteCurrent();
                break;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * setViewAnimated RealEstate in this fragment view<br>
     * if input == null : setViewAnimated standard value
     * @param realEstate to setViewAnimated
     */
    public void setViewAnimated(final RealEstate realEstate) {

        final CountDownLatch donelatch = new CountDownLatch(1);
        Animators.anim_remove(getActivity(), getView())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        donelatch.countDown();
                    }
                })
                .start();

        new PostponedUITask(getActivity(), donelatch){
            @Override
            public void runPostponedUITask() {
                setView(realEstate);

                View v = getView();
                if(v != null)
                    Animators.anim_add(getActivity(), v);
            }
        }.start();

    }

    /**
     * sets this view
     * @param realEstate
     */
    public void setView(RealEstate realEstate){
        setCurrentRealEstate(realEstate);

        setRealEstateView(realEstate);

        ArrayList<Photo> photos = null;
        if (realEstate != null){
            photos = (ArrayList<Photo>) realEstate.getPhotos();
            Collections.sort(photos, new Comparator<Photo>() {
                @Override
                public int compare(Photo lhs, Photo rhs) {
                    long lhstime, rhstime;
                    try{
                        lhstime = lhs.getDate().getTime();
                    } catch(NullPointerException e){ return 1;}
                    try{
                        rhstime = rhs.getDate().getTime();
                    } catch(NullPointerException e){return -1;}

                    if(lhstime == rhstime)
                        return 0;
                    if (lhstime < rhstime)
                        return -1;
                    else
                        return 1;
                }
            });
        }
        setPhotosView(realEstate, photos);

    }

    private void setRealEstateView(RealEstate realEstate){
        if (! isAdded()) return;


        String std_null = getResources().getString(R.string.realestate_std_value);

        String showings_str = std_null;     String address = std_null;    String type =std_null;
        String constructYear=std_null;      String startbid=std_null;     String rent=std_null;
        String floor=std_null;              String rooms=std_null;        String livingspace=std_null;
        String priceLivingspace=std_null;   String description=std_null;

        if (realEstate != null){

            if (realEstate.getShowings() != null && !realEstate.getShowings().isEmpty()){
                List<Event> showings = realEstate.getShowings();

                Collections.sort(showings, new Comparator<Event>(){
                    @Override
                    public int compare(Event lhs, Event rhs) {
                        return lhs.getDate().compareTo(rhs.getDate());
                    }
                });
                showings_str = combinedString("\n", showings);
            }

            address         = getString(realEstate.getAddress(), std_null);
            type            = getString(getResources().getString(realEstate.getType().getNameId()), std_null);
            constructYear   = getString(realEstate.getConstructYearString(), std_null);

            startbid        = String.valueOf(realEstate.getStartBid());
            rent            = String.valueOf(realEstate.getRent());
            floor           = String.valueOf(realEstate.getFloor());
            rooms           = String.valueOf(realEstate.getRooms());
            livingspace     = String.valueOf(realEstate.getLivingSpace());
            priceLivingspace = String.valueOf(realEstate.getStartBid() / realEstate.getLivingSpace());
            description     = getString(realEstate.getDescription(), std_null);

        }

        setViewText(R.id.realestate_address,            address);
        setViewText(R.id.realestate_type,               type);
        setViewText(R.id.realestate_constructYear,      constructYear);
        setViewText(R.id.realestate_showing,            showings_str);
        setViewText(R.id.realestate_startbid,           startbid);
        setViewText(R.id.realestate_rent,               rent);
        setViewText(R.id.realestate_floor,              floor);
        setViewText(R.id.realestate_rooms,              rooms);
        setViewText(R.id.realestate_livingspace,        livingspace);
        setViewText(R.id.realestate_priceLivingSpace,   priceLivingspace);
        setViewText(R.id.realestate_description,        description);

    }

    /**
     * displays photos in current view
     * @param realEstate
     * @param photos
     */
    public void setPhotosView(final RealEstate realEstate, final ArrayList<Photo> photos){
        if (photos == null || getCurrentRealEstate() == null) return;

        PhotoAdapter adapter = new PhotoAdapter(getActivity());

        try {
            GridView gridView = (GridView) getView().findViewById(R.id.gridView_photos);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Photo photo = photos.get(position);
                    if(photo.getPhotoFile() != null && photo.getPhotoFile().exists()){
                        startPhotoDisplayActivity(realEstate, photo);
                    } else {
                        //TODO add dialog msg
                        Dialog.question(getActivity(), "photo not found, delete this?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataMapper.delete(getActivity(), photo);
                            }
                        }).create().show();
                    }
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    //TODO flytta detta till photoadapter ??
                    final Photo CLICK_PHOTO = photos.get(position);
                    if(CLICK_PHOTO.getPhotoFile() != null && CLICK_PHOTO.getPhotoFile().exists()) {
                        Dialog.optionMenu(getActivity(),
                                getResources().getString(R.string.photo_title),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                DatabaseDialog.delete(getActivity(), CLICK_PHOTO, null).show();
                                                break;
                                            case 1:
                                                DatabaseDialog.edit(getActivity(), CLICK_PHOTO)
                                                        .show(getActivity().getSupportFragmentManager(), "update");
                                                break;
                                        }
                                    }
                                },
                                getResources().getString(R.string.delete_title), getResources().getString(R.string.edit_title)
                        ).create().show();
                    } else {
                        //TODO add dialog msg
                        Dialog.question(getActivity(), "photo not found, delete this?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataMapper.delete(getActivity(), CLICK_PHOTO);
                            }
                        }).create().show();
                    }

                    return true;
                }
            });
        } catch (NullPointerException e){}

    }

    /**
     * start activity PhotoDetailActivity
     * @param realEstate RealEstate from DatabaseFacade.entries to get Photos from
     * @param startDisplayPhoto Photo from photos to display at activity start
     */
    private void startPhotoDisplayActivity(final RealEstate realEstate, Photo startDisplayPhoto){
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        DataMapper.setCurrentRealEstate(realEstate);
        int photoidx = realEstate.getPhotos().indexOf(startDisplayPhoto);
        intent.putExtra(PhotoDetailActivity.BUNDLEKEY_CURRENTPHOTO, photoidx);

        startActivity(intent);
    }

    private static String getString(String s, String nullstring){
        if(s == null || s.replaceAll(" ", "").isEmpty())
            return nullstring;

        return s;
    }


    private String combinedString(String separator, List<Event> events){
        String res = "";
        for(int i = 0; i < events.size(); i++){
            res += events.get(i).getDateString(Event.DATEFORMAT_STDFORMAT);
            if(i < events.size() -1) // if not last item
                res += separator;
        }
        return res;
    }

    private RealEstate getCurrentRealEstate(){
        return DataMapper.getCurrentRealEstate();
    }


    private <TextContent> void setViewText(int viewId, TextContent textContent){
        String s        = textContent.toString();
        try {
            TextView tv = (TextView) getView().findViewById(viewId);
            tv.setText(s);
        } catch (NullPointerException e){
            Log.e("error", e.getMessage());
        }
    }

    private <TextContent> void setViewText(View rootView, int viewId, TextContent textContent){
        try {
            ((TextView) rootView.findViewById(viewId)).setText(textContent.toString());
        } catch (NullPointerException e){
            Log.e("error", e.getMessage());
        }
    }

    private void setCurrentRealEstate(RealEstate realEstate){
        DataMapper.setCurrentRealEstate(realEstate);

    }

    @Override
    public String getObserverId() {
        return getClass().toString();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void updateResetView() {
        Log.d("redetailsfrag", "updateRestView()");
        if(! isVisible()){
            return;
        }

        RealEstate re = getCurrentRealEstate();
        setViewAnimated(re);
        try {
            GridView gridView = (GridView) getActivity().findViewById(R.id.gridView_photos);
            ((PhotoAdapter) gridView.getAdapter()).notifyDataSetChanged();

        } catch(NullPointerException e){

        }


    }

    @Override
    public void updateAdd(DataAccessObject item) {

    }

    @Override
    public void updateDelete(DataAccessObject item) {


    }


    private void onEditCurrent(){
        if (isValidCurrentState()){
            DatabaseDialog.edit(getActivity(), DataMapper.getCurrentRealEstate())
                    .show(getActivity().getSupportFragmentManager(), "editRealEstate");
        }
    }

    /**
     * determine if current orientation is landscape
     * @return true if current orientation is landscape
     */
    private boolean isLandscapeOrientation(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * determines if current RealEstate state is valid<br>
     * vaild state is there is a list of RealEstate and a current RealEstate is selected<br>
     *     if invalid state, toastrmessage is displayed
     * @return true if current state is valid
     */
    private boolean isValidCurrentState(){
        String toastMsg = "";

        switch(DataMapper.getCurrentState()){
            case DataMapper.CURRENT_STATE_OK:
                return true;
            case DataMapper.CURRENT_STATE_LISTEMPTY:
                toastMsg = getString(R.string.realEstate_notFound);
                break;
            case DataMapper.CURRENT_STATE_NOTSELECTED:
                toastMsg = getString(R.string.realEstate_notSelected);
                break;
        }

        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void onDeleteCurrent(){
        if (isValidCurrentState()){
            DatabaseDialog.delete(getActivity(), DataMapper.getCurrentRealEstate(), new Runnable(){
                @Override
                public void run() {
                    if (! isLandscapeOrientation()){
                        if(getCurrentRealEstate() == null)
                            getActivity().finish();

                    }
                }
            }).show();

        }
    }

    private void onShowingsMenu(){
        if (isValidCurrentState()){
            DatabaseDialog.showingMenu(getActivity(), DataMapper.getCurrentRealEstate()).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_OK) return;

        switch (requestCode){
            case RealEstateDetailsFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:

                String photopath = mPhotoPath;

                PhotoFile photofile = new PhotoFile(photopath);

                if (photofile.exists()) {

                    DatabaseDialog.savePhoto(getActivity(), getCurrentRealEstate(), photofile, new DatabaseDialog.OnPost() {
                        @Override
                        public void run(Photo photo) {

                        }
                    }).show(getActivity().getSupportFragmentManager(), "savePhotoDialog");
                }
                break;

        }
    }


    /**
     * asks user for permission to start camera, if permission granted camera activity start for result<br>
     * to receive camera result use {@link #onActivityResult(int, int, Intent)} with requestcode {@link RealEstateDetailsFragment#CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE}
     */
    private void startCamera(){
        if(! Permission.askPermissionIfNeeded(getActivity(),
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return ;
        }

        final Activity act = getActivity();
        new PostponedUITask2(act){
            private String photopathBuf;
            @Override
            public void onWorkerThread() {
                String imgDir = Settings.getPhotoSource();
                File photofile       = PhotoFile.createNonexistingFile(imgDir);

                if(photofile != null)
                    photopathBuf = photofile.getAbsolutePath();
            }

            @Override
            public void onUIThread() {
                if(photopathBuf != null){
                    mPhotoPath = photopathBuf;
                    startActivityForResult(CameraTool.startCamera(act, mPhotoPath), CameraTool.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }

            }
        }.start();

    }


}
