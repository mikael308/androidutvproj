package com.example.mikael.androidutvproj.details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.Photo;

/**
 * Fragment displaying editable attributes of a Photo instance<br>
 *     to preset a Photo to edit: use {@link #newInstance(int, Photo)}
 *     if used as Dialog: implementation of DialogButtonListener is called on clickevents,
 *     {@link #setOnClickListener(DialogButtonListener)}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class PhotoAttributeDialogFragment extends DialogFragment {

    private static String BUNDLEKEY_TITLE = "title";

    private static String BUNDLEKEY_PHOTO = "photo";
    /**
     * this DialogButtonListener
     */
    private DialogButtonListener mDialogButtonListener = new DialogButtonListener();

    /**
     * current Photo
     */
    private Photo mPhoto;
    /**
     * this title
     */
    private String mTitle;

    public PhotoAttributeDialogFragment(){
        mPhoto = new Photo();
    }

    public static PhotoAttributeDialogFragment newInstance(int titleResId, Photo photo){
        PhotoAttributeDialogFragment dfrag = new PhotoAttributeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLEKEY_PHOTO, photo);
        bundle.putInt(BUNDLEKEY_TITLE, titleResId);
        dfrag.setArguments(bundle);

        return dfrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mPhoto = getArguments().getParcelable(BUNDLEKEY_PHOTO);
            int titleResId = getArguments().getInt(BUNDLEKEY_TITLE);
            mTitle = getResources().getString(titleResId);

        } catch(NullPointerException e){
            mPhoto = new Photo();
            mTitle = "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getShowsDialog()) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        View rootView = inflater.inflate(R.layout.layout_edit_attrs_photo, container);
        display(rootView, mPhoto);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_edit_attrs_photo, null);

        display(rootView, mPhoto);

        final AlertDialog AD = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(mTitle)
                .setPositiveButton(R.string.btn_ok,null) // see onShowListener
                .setNegativeButton(R.string.btn_cancel, null) // see onShowListener
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

    /**
     * get view
     * @param viewId
     * @return
     */
    private View getView(int viewId){
        if (getShowsDialog()){
            return getDialog().findViewById(viewId);
        } else {
            return getView().findViewById(viewId);
        }
    }

    /**
     * display Photo in View
     * @param rootView
     * @param photo
     */
    public void display(View rootView, Photo photo){
        mPhoto = photo;

        if(photo != null) {
            if (photo.getPhotoFile() != null) {
                ImageView iv = (ImageView) rootView.findViewById(R.id.image);
                iv.setImageBitmap(photo.getPhotoBitmap());
            }

            EditText edit_description = (EditText) rootView.findViewById(R.id.edit_description);
            edit_description.setText(photo.getDescription());
        }
    }

    /**
     * get this Photo
     * @return current photo as defined by current inputviews
     */
    public Photo getPhoto(){

        String description = ((EditText) getView(R.id.edit_description)).getText().toString();

        mPhoto.setDescription(description);

        return mPhoto;
    }

    /**
     * sets this clicklistener used by Dialog
     * @param dialogButtonListener
     * @return
     */
    public PhotoAttributeDialogFragment setOnClickListener(DialogButtonListener dialogButtonListener){
        mDialogButtonListener = dialogButtonListener;
        return this;
    }


}
