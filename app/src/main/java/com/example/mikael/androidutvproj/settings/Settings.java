package com.example.mikael.androidutvproj.settings;

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
     * shared preferenes main key : settings<br>
     *     main key to settings shared preferences
     */
    public static final String SHAREDPREFKEY_SETTINGS = "SETTINGS";
    /**
     * shared preferences settings key : this app language<br>
     *     store language code
     */
    public static final String SHAREDPREFKEY_LANGUAGE = "SHAREDPREFKEY_LANGUAGE";
    /**
     * shared preferences settings key : photosource<br>
     *     holds value where photos should be stored
     */
    public static final String SHAREDPREFKEY_PHOTOSOURCE = "SHAREDPREFKEY_PHOTOSOURCE";
    /**
     * shared preferences settings key : show dates<br>
     *     holds value for setting if events will be marked as by their state
     */
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

    /**
     * Settings shared preferences<br>
     *     see key {@link #SHAREDPREFKEY_SETTINGS}
     */
    private static SharedPreferences mSharedPrefs;


    private Settings(){

    }

    /**
     * initialize this Settings<br>
     * @param context
     */
    public static void initSettings(Context context){

        Log.d("hal", "a\ninitSettings : running init settings");
        mSharedPrefs = context.getSharedPreferences(SHAREDPREFKEY_SETTINGS, context.MODE_PRIVATE);

        String langcode = mSharedPrefs.getString(SHAREDPREFKEY_LANGUAGE, LANGUAGE_DEFAULT);
        if(setLanguage(context, langcode)){

        }

    }
    /**
     * set this configuration language
     * @param langcode new language langcode
     * @return true if new language was set correct
     */
    public static boolean setLanguage(Context context, String langcode){
        if (Lang.getByLangCode(langcode) == null
                || context == null) return false;

        Locale newLocale = new Locale(langcode);
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = newLocale;
        res.updateConfiguration(conf, res.getDisplayMetrics());

        return true;
    }

    /**
     * get the current displaylanguage<br>
     *     {@link #initSettings(Context)} must have been set in prior of calling this
     * @return
     */
    public static String getCurrentLangCode(){

        return mSharedPrefs.getString(SHAREDPREFKEY_LANGUAGE, LANGUAGE_DEFAULT);
    }

    /**
     * get the name of the current display language
     * @return
     */
    public static Lang getCurrentLang(){
        String langcode = getCurrentLangCode();
        return Lang.getByLangCode(langcode);
    }

    /**
     * get the photosource selected in settings
     * @return
     */
    public static String getPhotoSource(){
        return mSharedPrefs.getString(SHAREDPREFKEY_PHOTOSOURCE, PHOTOSRC_DEFAULT);
    }


}