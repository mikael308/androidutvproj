package com.example.mikael.androidutvproj.settings;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

    /**
     * determines if app has permission, if not: permission dialog is displayed to user
     * @param activity calling activity
     * @param permissions what permissions to check
     * @return the permission value according to param permission. Returns true if ALL permissions is granted, if n >= 1 permissions is denied: this method returns false
     */
    public static boolean askPermissionIfNeeded(Activity activity, String... permissions){

        boolean allPermGranted = true;
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                allPermGranted = false;
        }

        if(! allPermGranted){
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    200);
            for(String permission : permissions){
                if(! hasPermission(activity, permission))
                    allPermGranted = false;
            }
        }

        return allPermGranted;
    }
}
