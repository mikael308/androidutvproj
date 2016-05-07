package com.example.mikael.androidutvproj.settings;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * handles all permissions used in activity
 *
 * @author Mikael Holmbom
 * @version 1.0
 *
 */
public class Permission {


    /**
     * check if permission is set, returns the value according to prior set permission
     * @param activity calling activity
     * @param permission what permission to check
     * @return permission value. if value hasn't been set in prior: false is returned
     */
    public static boolean hasPermission(Activity activity, String permission){

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean askPermissionIfNeeded(Activity activity, String... permission){
        boolean allPermissionGranted = true;
        for(String p : permission){
            if(! askPermissionIfNeeded(activity, p))
                allPermissionGranted = false;

        }

        return allPermissionGranted;
    }

    /**
     * determines if app has permission, if not: permission dialog is displayed to user
     * @param activity calling activity
     * @param permission what permission to check
     * @return the permission value according to param permission. Returns true if permission is granted
     */
    public static boolean askPermissionIfNeeded(Activity activity, String permission){
        Log.d("hal", " * PERMISSION : " + permission);

        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        boolean permGranted = permissionCheck == PackageManager.PERMISSION_GRANTED;

        if(! permGranted){
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    200);
            permGranted = hasPermission(activity, permission);
            Log.d("hal", "  asked and now set to " + permGranted);
        }else {
            Log.d("hal", "  did not have to ask was " + permGranted);
        }


        return permGranted;
    }
}
