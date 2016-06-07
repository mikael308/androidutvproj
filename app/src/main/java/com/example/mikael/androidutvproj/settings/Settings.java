package com.example.mikael.androidutvproj.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.util.Locale;

/**
 *
 * keys used for settings
 * @author Mikael Holmbom
 * @version 1.0
 *
 */
public class Settings {

    /////////////////
    // KEYS
    ////////////////

    /**
     * shared preferenes main key : settings
     */
    public static final String SHAREDPREFKEY_SETTINGS = "SETTINGS";
    /**
     * shared preferences settings key : this app language<br>
     *     store language code
     */
    public static final String SHAREDPREFKEY_LANGUAGE = "SHAREDPREFKEY_LANGUAGE";
    /**
     * shared preferences settings key : photosource
     */
    public static final String SHAREDPREFKEY_PHOTOSOURCE = "SHAREDPREFKEY_PHOTOSOURCE";

    public static final String SHAREDPREFKEY_SHOWDATES = "SHAREDPREFKEY_SHOWDATES";


    ///////////////////
    // DEFAULT VALUES
    ///////////////////

    /**
     * default preference value : language
     */
    public static final String LANGUAGE_DEFAULT = Lang.ENGLISH.getLangCode();
    /**
     * default preference value : photosource
     */
    public static final String PHOTOSRC_DEFAULT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();



    private Settings(){

    }


    public static String getPhotoSource(Activity activity){
        return activity.getSharedPreferences(SHAREDPREFKEY_SETTINGS, activity.MODE_PRIVATE)
                .getString(SHAREDPREFKEY_PHOTOSOURCE, PHOTOSRC_DEFAULT);
    }

}
