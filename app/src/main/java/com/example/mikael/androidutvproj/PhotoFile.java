package com.example.mikael.androidutvproj;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.settings.Settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    public static File getNewFile(String directoryPath){
        int nExtra = 0;
        String fileExt = ".jpg";
        File f;
        while(true) {
            String fileNum = String.format("%03d", DataMapper.getCurrentRealEstate().getPhotos().size() + 1 + nExtra);
            String filename = String.format("img_%s_%s" + fileExt,
                    DataMapper.getCurrentRealEstate().toString(),
                    fileNum);

            f = new File(directoryPath, filename);

            if(f.exists())
                nExtra++;
            else
                return f;
        }
    }


    /**
     * Save Bitmap to file
     * @param bm
     * @return
     */
    public static File saveToFile(Activity activity, Bitmap bm){
        String stdImgDir    = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        String imgDir       = activity.getSharedPreferences(Settings.SHAREDPREFKEY_SETTINGS, activity.MODE_PRIVATE).getString(Settings.SHAREDPREFKEY_PHOTOSOURCE, stdImgDir);

        File f = getNewFile(imgDir);
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

}
