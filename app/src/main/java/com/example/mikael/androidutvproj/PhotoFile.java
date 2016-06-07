package com.example.mikael.androidutvproj;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.settings.Settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public class PhotoFile extends File {


    public PhotoFile(String path) {
        super(path);
    }


    /**
     * generate a file with unique filename within directory param, file is created in directory and returned
     * @param directoryPath what directory to add file
     * @return created file
     */
    public static File createNonexistingFile(String directoryPath){

        String path = getNonExistingFilepath(directoryPath, DataMapper.getCurrentRealEstate());

        File f = new File(path);
        try{
            f.createNewFile();

            Log.d("photofile", "creating file : " + path);
            return f;

        } catch(IOException e){

        }

        return null;
    }

    public static String getNonExistingFilepath(String directoryPath, RealEstate re){
        int nExtra = 1;
        String fileExt = ".jpg";
        File dir = new File(directoryPath);
        if(! dir.exists() || !dir.isDirectory()){
            return null;
        }

        File f;
        while(true) {
            String fileNum = String.format("%03d", re.getPhotos().size() + nExtra);
            String filename = String.format("img_%s_%s",
                    DataMapper.getCurrentRealEstate().toString(),
                    fileNum);

            f = new File(directoryPath, filename + fileExt);

            if (f.exists()){
                nExtra++;
            } else {
                return f.getAbsolutePath();
            }
        }
    }


    /**
     * Save Bitmap to file
     * @param bm
     * @return
     */
    public static File saveToFile(Activity activity, Bitmap bm){
        String stdImgDir    = Settings.getPhotoSource();
        String imgDir       = activity.getSharedPreferences(Settings.SHAREDPREFKEY_SETTINGS, activity.MODE_PRIVATE).getString(Settings.SHAREDPREFKEY_PHOTOSOURCE, stdImgDir);

        File f = createNonexistingFile(imgDir);
        try{
            if(f.createNewFile()){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                return f;
            }

        } catch(Exception e){
            Log.e("error", "exception : " + e.getMessage());
        }

        return null;
    }

    public Bitmap scaleToFit(int width, int height){
        if (getAbsolutePath() == null || getAbsolutePath().isEmpty())
            return null;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/width, photoH/height);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(getAbsolutePath(), bmOptions);

    }

}
