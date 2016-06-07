package com.example.mikael.androidutvproj.database;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mikael.androidutvproj.Dialog;
import com.example.mikael.androidutvproj.tool.PostponedUITask;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.dao.Event;
import com.example.mikael.androidutvproj.dao.Photo;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.details.DialogButtonListener;
import com.example.mikael.androidutvproj.details.PhotoAttributeDialogFragment;
import com.example.mikael.androidutvproj.details.RealEstateAttributeDialog;
import com.example.mikael.androidutvproj.settings.Permission;
import com.example.mikael.androidutvproj.tool.PostponedUITask2;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class DatabaseDialog extends com.example.mikael.androidutvproj.Dialog{

    /**
     * start dialog to delete photo from database
     * @param activity
     * @param photo
     * @param postDelete runs after photo is deleted if user click ok
     * @return
     */
    public static AlertDialog delete(final Activity activity, final Photo photo, final Runnable postDelete){

        LinearLayout dialog_view    = new LinearLayout(activity);
        final ImageView imgView     = new ImageView(activity);
        imgView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        dialog_view.addView(imgView);


        new PostponedUITask2(activity){
            private Bitmap bitmap;
            @Override
            public void onWorkerThread() {
                final int ivW = activity.getResources().getInteger(R.integer.photo_dialog_database_thumbnail_size);
                final int ivH = activity.getResources().getInteger(R.integer.photo_dialog_database_thumbnail_size);

/*                final int ivW = Math.min(activity.getResources().getInteger(R.integer.photo_dialog_database_thumbnail_size),
                        imgView.getLayoutParams().width);
                final int ivH = Math.min(activity.getResources().getInteger(R.integer.photo_dialog_database_thumbnail_size),
                        imgView.getLayoutParams().height);*/

                bitmap = photo.getPhotoFile().scaleToFit(ivW, ivH);
            }

            @Override
            public void onUIThread() {
                imgView.setImageBitmap(bitmap);
            }
        }.start();

        return confirm(activity,
                activity.getResources().getString(R.string.delete_title),
                initialUpper(activity.getResources().getString(R.string.delete_photo)),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new PostponedUITask(activity, DataMapper.delete(activity, photo)){
                            @Override
                            public void runPostponedUITask() {
                                if (postDelete!= null)
                                    postDelete.run();
                            }
                        }.start();

                    }
                })
                .setView(dialog_view)
                .setIcon(R.drawable.ic_delete_black_24dp)
                .create();
    }

    /**
     * create menu of current showings from realEstate available to delete
     * @param activity
     * @param realEstate
     * @return
     */
    public static AlertDialog deleteShowingMenu(final Activity activity, final RealEstate realEstate){
        ArrayList<String> menulist = new ArrayList<>();
        for(Event showing : realEstate.getShowings())
            menulist.add(showing.getDateString(Event.DATEFORMAT_STDFORMAT));

        final Resources res = activity.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, menulist);
        builder
                .setTitle(res.getString(R.string.showing_title))
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int WHICH_LISTITEM = which;

                        String titlesuffix  = res.getString(R.string.delete_title);
                        String confirmmsg   = res.getString(R.string.confirm) + " " + res.getString(R.string.showing_delete) + ": " + realEstate.getShowings().get(which);
                        Dialog.confirm(activity, titlesuffix, confirmmsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataMapper.delete(activity, realEstate.getShowings().get(WHICH_LISTITEM));

                            }
                        }).create().show();

                    }
                })
                .setNegativeButton(res.getString(R.string.btn_cancel), null);

        return builder.create();
    }


    /**
     * create delete entry request createDialog<br>
     *     createDialog makes two options
     *     <ul>
     *         <li>ok      - delete selectedEvent</li>
     *         <li>cancel  - nothing</li>
     *     </ul>
     * @param selectedRealEstate
     * @return
     */
    public static AlertDialog delete(final Activity activity, final RealEstate selectedRealEstate, final Runnable postDelete) {
        return createDialog(activity, R.string.settings_delete_title, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new PostponedUITask(activity, DataMapper.delete(activity, selectedRealEstate)){
                    @Override
                    public void runPostponedUITask() {
                        if (postDelete != null)
                            postDelete.run();
                    }
                }.start();
            }
        })
                .setMessage(activity.getResources().getString(R.string.settings_delete_message) + " " + selectedRealEstate.getAddress())
                .create();

    }

    /**
     * create dialog to delete RealEstate instance
     * @param activity
     * @param selectedRealEstate
     * @return
     */
    public static AlertDialog delete(final Activity activity, final RealEstate selectedRealEstate){
        return delete(activity, selectedRealEstate, null);
    }

    /**
     * create dialog to create a showing
     * @param activity
     * @param realEstate instance to add showing to
     * @param postAdd action after showing is added
     * @return
     */
    public static AlertDialog addShowing(final Activity activity, final RealEstate realEstate, final Runnable postAdd){
        final DatePicker datePicker = new DatePicker(activity);
        datePicker.setSpinnersShown(true);

        return createDialog(activity, R.string.entrydialog_newshowing_title,
                datePicker,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TimePicker timePicker = new TimePicker(activity.getApplicationContext());
                        timePicker.setIs24HourView(true);

                        createDialog(activity, R.string.entrydialog_newshowing_title,
                                timePicker,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Calendar cal = Calendar.getInstance();
                                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                                                timePicker.getCurrentHour(), timePicker.getCurrentMinute());

                                        Event newShowing = new Event()
                                                .setDate(cal.getTime());
                                        newShowing.setForeignKey(realEstate.getId());

                                        DataMapper.add(activity, newShowing);
                                        if (postAdd != null)
                                            postAdd.run();
                                    }
                                }).create().show();
                    }
                }).create();

    }

    /**
     * create dialog with options to handle realEstates showings
     * @param activity
     * @param realEstate
     * @return
     */
    public static AlertDialog showingMenu(final Activity activity, final RealEstate realEstate){
        ArrayList<String> menulist = new ArrayList<>();

        menulist.add(activity.getResources().getString(R.string.edit_title));
        menulist.add(activity.getResources().getString(R.string.delete_title));
        menulist.add(activity.getResources().getString(R.string.new_title));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, menulist);
        builder
                .setTitle(activity.getResources().getString(R.string.showing_title))
                .setIcon(R.drawable.ic_event_note_black_24dp)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //TODO fixa update showings update(activity, );
                                break;
                            case 1:
                                if (realEstate.getShowings().isEmpty()) {
                                    Toast.makeText(activity, activity.getString(R.string.showings_notFound), Toast.LENGTH_LONG).show();
                                } else {
                                    deleteShowingMenu(activity, realEstate).show();
                                }
                                break;
                            case 2:
                                addShowing(activity, realEstate, null).show(); //TODO du shiftar mellan att använda showdialog och dialog.show()
                                break;
                        }
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.btn_cancel), null);

        return builder.create();
    }

    /**
     * create listitem createDialog<br>
     * @param realEstate
     * @return createDialog
     */
    public static AlertDialog realEstateListitem(final AppCompatActivity activity, final RealEstate realEstate){
        ArrayList<String> menulist = new ArrayList<>();
        menulist.add(activity.getResources().getString(R.string.edit_title));
        menulist.add(activity.getResources().getString(R.string.delete_title));
        menulist.add(activity.getResources().getString(R.string.showing_title));

        LinearLayout layout = new LinearLayout(activity);
        for(String option : menulist){
            TextView optionitem = new TextView(activity);
            optionitem.setText(option);
            layout.addView(optionitem);
        }
        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, menulist);
        builder
                .setTitle(realEstate.getAddress())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:     edit(activity, realEstate).show(activity.getSupportFragmentManager(), "editRealEstate");
                                break; //TODO denna kan inte visas, eftersom den inte finns i en activity.. måste ha getSupportFragmentManager
                            case 1:     delete(activity, realEstate).show();        break;
                            case 2:     showingMenu(activity, realEstate).show();   break;
                        }
                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.btn_cancel), null);

        return builder.create();
    }

    /**
     * create dialog to edit realEstates attributes
     * @param activity
     * @param realEstate
     * @return
     */
    public static RealEstateAttributeDialog edit(final Activity activity, RealEstate realEstate){
        final RealEstateAttributeDialog attrDialog = RealEstateAttributeDialog.newInstance(realEstate);
        attrDialog.setOnClickListener(new DialogButtonListener() {
            @Override
            public boolean onPositiveClick() {
                RealEstate re = attrDialog.getRealEstate();

                if (!isValid(activity, re))
                    return false;

                DataMapper.edit(activity, re);

                return true;
            }
        });

        return attrDialog;
    }

    /**
     * create dialog to edit photos attributes
     * @param activity
     * @param photo
     * @return
     */
    public static PhotoAttributeDialogFragment edit(final Activity activity, final Photo photo){
        return edit(activity, photo, null);
    }

    /**
     * create dialog to edit photo attributes
     * @param activity
     * @param photo
     * @param postEdit action executed after edit is done
     * @return
     */
    public static PhotoAttributeDialogFragment edit(final Activity activity, final Photo photo, final Runnable postEdit){

        final PhotoAttributeDialogFragment attrDialog = PhotoAttributeDialogFragment.newInstance(R.string.photo_edit, photo);
        attrDialog.setOnClickListener(new DialogButtonListener() {
            @Override
            public boolean onPositiveClick() {
                Photo photo = attrDialog.getPhoto();

                if (!isValid(activity, photo))
                    return false;

                new PostponedUITask(activity, DataMapper.edit(activity, photo)){
                    @Override
                    public void runPostponedUITask() {
                        if(postEdit != null)
                            postEdit.run();
                    }
                }.start();

                return true;
            }
        });
        return attrDialog;
    }

    /**
     * create dialog to create new RealEstate instance
     * @param activity
     * @return
     */
    public static RealEstateAttributeDialog newRealEstate(final Activity activity){

        final RealEstateAttributeDialog dfrag  = RealEstateAttributeDialog.newInstance(new RealEstate());
        dfrag.setOnClickListener(new DialogButtonListener(){
            @Override
            public boolean onPositiveClick() {
                RealEstate re;
                try {
                    re = dfrag.getRealEstate();
                } catch(Exception e){
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (! isValid(activity, re))
                    return false;

                DataMapper.add(activity, re);
                return true;
            }
        });
        return dfrag;
    }
    //TODO tmp för dbg, du använder denna för att skicka in random aelestate
    public static RealEstateAttributeDialog newRealEstate(final Activity activity, RealEstate realEstate){

        final RealEstateAttributeDialog dfrag  = RealEstateAttributeDialog.newInstance(realEstate);
        dfrag.setOnClickListener(new DialogButtonListener(){
            @Override
            public boolean onPositiveClick() {
                RealEstate re = dfrag.getRealEstate();

                if (! isValid(activity, re))
                    return false;

                DataMapper.add(activity, re);
                return true;
            }
        });
        return dfrag;
    }

    public interface OnPost {

        void run(Photo photo);

    }

    /**
     * determines if Photo is valid<br>
     *     if Photo is not valid, toastmessage is shown and false is return
     * @param photo
     * @return true if Photo is valid
     */
    private static boolean isValid(final Activity activity, final Photo photo){

        if (photo.getDescription() == null || photo.getDescription().isEmpty()){
            Toast.makeText(activity, activity.getString(R.string.invalid_description), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (photo.getPhotoPath() == null || photo.getPhotoPath().isEmpty()){
            Toast.makeText(activity, activity.getString(R.string.invalid_photopath), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * determine if re is valid RealEstate
     * @param activity
     * @param re
     * @return
     */
    private static boolean isValid(final Activity activity, final RealEstate re){

        if (re.getAddress() == null || re.getAddress().isEmpty()){
            Toast.makeText(activity, activity.getString(R.string.invalid_address), Toast.LENGTH_LONG).show();
            return false;
        }
        if (re.getDescription() == null || re.getDescription().isEmpty()){
            Toast.makeText(activity, activity.getString(R.string.invalid_description), Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    /**
     * determine if event is valid Event
     * @param activity
     * @param event
     * @return
     */
    private static boolean isValid(final Activity activity, final Event event){
        if(event.getDate() == null){
            Toast.makeText(activity, activity.getString(R.string.invalid_description), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    /**
     *
     * @param activity
     * @param fk the foreign key
     * @param photofile
     * @param postSave
     */
    public static PhotoAttributeDialogFragment savePhoto(final Activity activity, final DataAccessObject fk, final File photofile, final OnPost postSave){

        Photo photo = new Photo()
                .setDate(new Date())
                .setPhotoFile(photofile);
        photo.setForeignKey(fk.getId());

        Permission.askPermissionIfNeeded(activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        final PhotoAttributeDialogFragment photoAttrDialog = PhotoAttributeDialogFragment.newInstance(R.string.photo_new, photo);
        photoAttrDialog.setOnClickListener(new DialogButtonListener() {
            @Override
            public boolean onPositiveClick() {
                final Photo dPhoto = photoAttrDialog.getPhoto();

                if(! isValid(activity, dPhoto))
                    return false;

                new PostponedUITask(activity, DataMapper.add(activity, dPhoto)){
                    @Override
                    public void runPostponedUITask() {
                        if(postSave != null) postSave.run(dPhoto);
                    }
                }.start();

                return true;
            }

            @Override
            public boolean onNegativeClick() {
                photoAttrDialog.getPhoto().getPhotoFile().delete();
                return true;
            }
        });

        return photoAttrDialog;
    }


}
