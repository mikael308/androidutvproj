package com.example.mikael.androidutvproj.tool;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.settings.Permission;

import java.io.File;

/**
 * handling camera device
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class CameraTool {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    /**
     * asks user for permission to start camera, if permission granted camera activity start for result<br>
     * to receive camera result use getActivityResult with requestcode {@link #CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE}
     */
    public static Intent startCamera(final Activity activity, final String photofilepath){

        if (! activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(activity, activity.getString(R.string.no_camera_msg), Toast.LENGTH_SHORT).show();
            return null;
        }

        if(! Permission.askPermissionIfNeeded(activity,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return null;
        }

        final Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentCamera.resolveActivity(activity.getPackageManager()) != null) {

            if (photofilepath != null) {
                File photofile = new File(photofilepath);
                if(! photofile.exists())
                    return null;

                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photofile));

                return intentCamera;
            }
        }

        return null;
    }
}
