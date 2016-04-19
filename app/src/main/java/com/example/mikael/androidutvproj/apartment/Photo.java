/**
* @author Mikael Holmbom - miho1202
* @version 1.0 
* class used for holding information about a Photo
* containing information : 
*	- content description
*	- destination of photo
*	- date photo was taken
*/

package com.example.mikael.androidutvproj.apartment;



import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Date;


public class Photo {

    private File mPhotoFile;
    /**
     * description of the content of this photo
     * */
    private String  mDescription;
    /**
     * destination of this photo
     */
    private LatLng  mLatLng;
    /**
     * date this photo was taken
     */
    private Date    mDate;

    /**
     * creates Photo object with date as current date and time
     * @param photofile file containing photo
     * @param description description of this photo
     * @param latlng destination of this photo
     */
    public Photo(File photofile, String description, LatLng latlng){
        mPhotoFile      = photofile;
        mDescription    = description;
        mLatLng         = latlng;
        mDate           = new Date();
    }
    /**
     * std ctor
     * @param photofile file containing photo
     * @param description description of this photo
     * @param latlng destination of this photo
     * @param date date this photo was taken
     */
    public Photo(File photofile, String description, LatLng latlng, Date date){
        mPhotoFile      = photofile;
        mDescription    = description;
        mLatLng         = latlng;
        mDate           = date;

    }

    /**
     * get this photofile
     * @return  photofile
     */
    public File getPhotoFile(){
        return mPhotoFile;
    }
    /**
     * get this description
     * @return description
     */
    public String getDescription(){
        return mDescription;
    }
    /**
     * gets this lattitude & Longitude as <code>LatLng</code>
     * where this photo were taken
     * @return LatLng of this photo
     */
    public LatLng getLatLng(){
        return mLatLng;
    }

    /**
     * get this date
     * @return this date of photo
     */
    public Date getDate(){
        return mDate;
    }

    public String toString(){
        return mPhotoFile.getName();
    }


}
