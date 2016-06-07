package com.example.mikael.androidutvproj.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikael.androidutvproj.PhotoFile;
import com.example.mikael.androidutvproj.anim.Animators;
import com.example.mikael.androidutvproj.settings.Permission;
import com.example.mikael.androidutvproj.tool.PostponedUITask2;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.Photo;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DatabaseDialog;
import com.example.mikael.androidutvproj.details.ImageNavigatorFragment;
import com.example.mikael.androidutvproj.details.RealEstateDetailsFragment;
import com.example.mikael.androidutvproj.settings.Settings;
import com.example.mikael.androidutvproj.tool.CameraTool;

import java.io.File;
import java.util.List;

/**
 * Activity displays a serie of photos.<br>
 *     first photo to setViewAnimated on start defined in intent call, use {@link #BUNDLEKEY_CURRENTPHOTO}
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @see com.example.mikael.androidutvproj.dao.Photo
 */
public class PhotoDetailActivity extends AppCompatActivity
        implements ImageNavigatorFragment.ImageNavigatorListener {


    public static String BUNDLEKEY_CURRENTPHOTO = "CURRENT_PHOTO";
    /**
     * background resource showing when no photo to display
     */
    private int std_background = android.R.color.transparent;
    /**
     * resource showing when photo bitmap is scaling
     */
    private int std_loadingphoto = R.drawable.ic_hourglass_empty_black_24dp;
    /**
     * index of current photo from DataMappers list of photos
     */
    private int mCurrentPhoto_idx = -1;
    /**
     * absolute path to last taken photo from devices camera
     */
    private String mPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_details_photo);

        setTitle(getTitle() + " : " + getCurrentRealEstate().getAddress());

        Intent args = getIntent();
        if(args != null){
            final int photoidx = args.getIntExtra(BUNDLEKEY_CURRENTPHOTO, -1);

            display(photoidx);
        }

        updateNavigator();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_new:
                startCamera();
                break;
            case R.id.action_delete:
                onDelete(getCurrentPhoto());
                break;
            case R.id.action_edit:
                onEdit(getCurrentPhoto());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * set current Photo
     * @param photo
     * @return true if photo was found in current list of Photos
     */
    public boolean setCurrentPhoto(Photo photo){
        mCurrentPhoto_idx = getCurrentRealEstate().getPhotos().indexOf(photo);
        return mCurrentPhoto_idx >= 0;
    }

    /**
     * get current RealEstate from DataMapper
     * @return
     */
    private RealEstate getCurrentRealEstate(){
        return DataMapper.getCurrentRealEstate();
    }

    /**
     * get the current Photo according to current index: {@link #mCurrentPhoto_idx}
     * @return current Photo, if no current Photo could be set, null is returned
     */
    public Photo getCurrentPhoto(){
        try{
            return getCurrentRealEstate().getPhotos().get(mCurrentPhoto_idx);

        } catch (NullPointerException | ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    private List<Photo> getPhotos(){
        return getCurrentRealEstate().getPhotos();
    }

    /**
     * get a Bitmap from photos scaled to fit current imagedisplays size
     * @param photo
     * @return
     */
    private Bitmap getScaledBitmap(final Photo photo){

        ImageView imageDisplay = (ImageView) getImageNavigator().getActivity().findViewById(R.id.imagedisplay);
        final int idW   = imageDisplay.getLayoutParams().width;
        final int idH   = imageDisplay.getLayoutParams().height;

        return photo.getPhotoFile().scaleToFit(idW, idH);
    }

    /**
     * asks user for permission to start camera, if permission granted camera activity start for result<br>
     * to receive camera result use {@link #onActivityResult(int, int, Intent)} with requestcode {@link RealEstateDetailsFragment#CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE}
     */
    private void startCamera(){
        if(! Permission.askPermissionIfNeeded(this,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return;
        }

        final Activity act = this;
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

    private void onEdit(final Photo photo){
        if(photo == null){
            Toast.makeText(this, getString(R.string.photos_notFound), Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseDialog.edit(this, photo, new Runnable() {
            @Override
            public void run() {
                display(photo);
            }
        })
                .show(getSupportFragmentManager(), "update");
    }

    private void onDelete(final Photo photo){
        if(photo == null){
            Toast.makeText(this, getString(R.string.photos_notFound), Toast.LENGTH_SHORT).show();
            return;
        }

        final Photo currentPhoto = getCurrentPhoto();
        final Activity activity = this;
        DatabaseDialog.delete(this, photo, new Runnable() {
            @Override
            public void run() {

                if(currentPhoto.equals(photo)){ // onDelete item was current
                    getImageNavigator().navigateRight();
                }

                DatabaseDialog.removeFile(activity, currentPhoto.getPhotoFile(), null).show();

                updateNavigator();
            }
        }).show();

    }

    private ImageNavigatorFragment getImageNavigator(){
        return (ImageNavigatorFragment) getSupportFragmentManager().findFragmentById(R.id.imageNavigator);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        Log.d("photodetailactivity", "resultcode == OK, requestcode : " + requestCode);

        switch (requestCode){
            case RealEstateDetailsFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:

                String photopath = mPhotoPath;
                Log.d("redetailfrag", "photopath : "+ photopath);

                PhotoFile photofile = new PhotoFile(photopath);

                if (photofile.exists()) {
                    Log.d("redetilsfrag", "my tmp file  : " + photofile.getAbsolutePath());

                    DatabaseDialog.savePhoto(this, getCurrentRealEstate(), photofile, new DatabaseDialog.OnPost() {
                        @Override
                        public void run(Photo photo) {
                            display(photo);
                            updateNavigator();
                        }
                    })
                            .show(getSupportFragmentManager(), "savePhotoDialog");
                }
                break;

        }
    }

    /**
     * increase current photo index
     */
    private void incrIdx(){
        if(getPhotos().isEmpty()){
            mCurrentPhoto_idx = -1;
        } else {
            mCurrentPhoto_idx = ++mCurrentPhoto_idx % getPhotos().size();
        }
    }
    /**
     * decrease current photo index
     */
    private void decrIdx(){
        if(getPhotos().isEmpty()){
            mCurrentPhoto_idx = -1;

        } else {
            mCurrentPhoto_idx--;
            while (mCurrentPhoto_idx < 0)
                mCurrentPhoto_idx += getPhotos().size();
        }
    }

    @Override
    public void onNavigateLeft(){
        decrIdx();
        display(getCurrentPhoto());
    }

    @Override
    public void onNavigateRight(){
        incrIdx();
        display(getCurrentPhoto());
    }

    private void updateNavigator(){
        getImageNavigator().setNavigateEnabled(getPhotos().size() > 1);
    }

    /**
     * display photo of index idx from current RealEstates list of Photos
     * @param idx
     */
    public void display(int idx) {
        try {
            display(getPhotos().get(idx));
        } catch (IndexOutOfBoundsException e){
            display(null);
        }
    }

    /**
     * displays photo<br>
     *     if param photo is null: display empty view
     * @param photo
     */
    public void display(final Photo photo){
        setCurrentPhoto(photo);
        animateSwitchInfo(photo);

        getImageNavigator().display(std_loadingphoto);

        new PostponedUITask2(this){
            private Bitmap bitmap;
            @Override
            public void onWorkerThread() {
                if(photo != null){
                    bitmap = getScaledBitmap(photo);
                }
            }

            @Override
            public void onUIThread() {
                if(photo != null && bitmap != null){
                    getImageNavigator().display(bitmap);

                } else {
                    getImageNavigator().display(std_background);
                }
            }
        }.start();

    }

    /**
     * use {@link #submitInfo(Photo)} with animation
     * @param photo
     */
    private void animateSwitchInfo(final Photo photo){
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.photo_info);

        Animators.fadeInOut(infoLayout, 200, 0, 0, new Runnable() {
            @Override
            public void run() {
                submitInfo(photo);
            }
        }).start();

    }

    /**
     * sets this current info setViewAnimated to info from photo
     * @param photo attributes to set info
     */
    private void submitInfo(Photo photo){
        TextView index_textview = (TextView) findViewById(R.id.header_index);

        int idx = getPhotos().indexOf(photo);
        String text = getResources().getString(R.string.index_header) + " " + (idx +1) + " (" + getPhotos().size() + ")";
        index_textview.setText(text);

        TextView date           = (TextView) findViewById(R.id.header_date);
        TextView description    = (TextView) findViewById(R.id.textview_description);

        if (photo == null){
            photo = new Photo();
        }

        String strDate = photo.getDate() == null ?                  "" : photo.getDate().toString();
        String strDescription = photo.getDescription() == null ?    "" : photo.getDescription();

        date.setText(strDate);
        description.setText(strDescription);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentPhoto_idx = savedInstanceState.getInt(BUNDLEKEY_CURRENTPHOTO);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLEKEY_CURRENTPHOTO, getCurrentPhoto());
    }


}
