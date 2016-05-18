package com.example.mikael.androidutvproj.display;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 *     if used as Dialog: implementation of Runnable set in
 *     {@link #setOnPositiveClick(Runnable)} is called on positive button click
 * @author Mikael Holmbom
 * @version 1.0
 */
public class PhotoAttributeDialogFragment extends DialogFragment {

    private static String BUNDLEKEY_TITLE = "title";

    private static String BUNDLEKEY_PHOTO = "photo";

    /**
     * get called on dialog positive click
     */
    private Runnable mOnPositiveClick;
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

        if(! getShowsDialog()){
            View rootView = inflater.inflate(R.layout.layout_edit_attrs_photo, container);

            display(rootView, mPhoto);

            return rootView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_edit_attrs_photo, null);

        display(rootView, mPhoto);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.photo_edit)
                .setView(rootView)
                .setTitle(mTitle)
                .setPositiveButton(R.string.btn_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(mOnPositiveClick != null)
                                    mOnPositiveClick.run();

                            }
                        }
                )
                .setNegativeButton(R.string.btn_cancel, null)
                .create();
    }

    /**
     * gets view from this dialog
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
     * @return
     */
    public Photo getPhoto(){

        String description = ((EditText) getView(R.id.edit_description)).getText().toString();

        mPhoto.setDescription(description);

        return mPhoto;
    }

    /**
     * sets this positive click runnable. param Runnable is called on Dialog positive click
     * @param onPositiveClick
     * @return
     */
    public PhotoAttributeDialogFragment setOnPositiveClick(Runnable onPositiveClick){
        mOnPositiveClick = onPositiveClick;
        return this;
    }


}
