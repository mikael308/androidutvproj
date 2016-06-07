package com.example.mikael.androidutvproj.details;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mikael.androidutvproj.tool.PostponedUITask2;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.Photo;


/**
 * adapter holding DataMappers current RealEstates list of Photos
 * @author Mikael Holmbom
 * @version 1.0
 */
public class PhotoAdapter extends BaseAdapter {



    private int std_photonotfound = R.drawable.ic_report_black_24dp;

    private Activity mActivity;

    public PhotoAdapter(Activity activity){
        mActivity   = activity;
    }

    @Override
    public int getCount() {
        try {
            return DataMapper.getCurrentRealEstate().getPhotos().size();
        } catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        try{
            return DataMapper.getCurrentRealEstate().getPhotos().get(position);
        } catch(NullPointerException e){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Resources res = mActivity.getResources();
        final int height  = res.getInteger(R.integer.photo_gridview_thumbnail_size);
        final int width   = res.getInteger(R.integer.photo_gridview_thumbnail_size);

        if (convertView == null){

            imageView = new ImageView(mActivity);
            imageView.setBackgroundResource(R.drawable.ic_hourglass_empty_black_24dp);
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }



        final Photo photo = (Photo) getItem(position);
        final ImageView IV = imageView;

        new PostponedUITask2(mActivity){
            private Bitmap bitmap = null;
            @Override
            public void onWorkerThread() {
                try{
                    bitmap = photo.getPhotoFile().scaleToFit(width, height);
                } catch(NullPointerException e){

                }
            }

            @Override
            public void onUIThread() {
                if(bitmap != null){
                    IV.setImageBitmap(bitmap);
                } else {
                    //TODO add toastmsg
                    Toast.makeText(mActivity, "photo " + photo.getPhotoPath() + " could not be found", Toast.LENGTH_SHORT);

                    IV.setBackgroundResource(std_photonotfound);
                }
            }

        }.start();

        return imageView;
    }

}
