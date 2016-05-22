/**
* @author Mikael Holmbom - miho1202
* @version 1.0 
* class used for holding information about a Photo
* containing information : 
*	- content description
*	- destination of photo
*	- date photo was taken
*/

package com.example.mikael.androidutvproj.dao;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Date;


public class Photo extends DataAccessObject{


    /**
     * this photo file
     */
    private File mPhotoFile;
    /**
     * description of the content of this photo
     * */
    private String  mDescription;
    /**
     * destination of this photo
     */
    private LatLng  mLatLng = null;
    /**
     * date this photo was taken
     */
    private Date    mDate = null;


    public Photo(){
        super("photo-"+RandomString.getRandomString(20));
    }

    public Photo(String id, File photoFile) {
        super(id);
        mPhotoFile = photoFile;
    }

    public Photo(File photoFile){
        super("photo-"+RandomString.getRandomString(20));
        mPhotoFile = photoFile;
    }


    /**
     * std ctor
     * @param photofile file containing photo
     * @param description description of this photo
     * @param latlng destination of this photo
     * @param date date this photo was taken
     */
    public Photo(File photofile, String description, LatLng latlng, Date date){
        super("photo-"+RandomString.getRandomString(20));

        mPhotoFile      = photofile;
        mDescription    = description;
        mLatLng         = latlng;
        mDate           = date;
    }


    /**
     * get this photofile
     * @return  photofile
     */
    public Bitmap getPhotoBitmap(){
        return BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
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
     * gets this LatLng where this photo were taken
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

    public Photo setPhotoFile(File f){
        mPhotoFile = f;
        return this;
    }

    public Photo setDescription(String description){
        mDescription = description;
        return this;
    }

    public Photo setLatLng(LatLng latLng){
        mLatLng = latLng;
        return this;
    }

    public Photo setDate(Date d){
        mDate = d;
        return this;
    }

    public String toString(){
        return mPhotoFile.getPath();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Photo)
            return getId().equals(((Photo) o).getId());

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);

        parcel.writeStringArray(new String[]{
                getDescription(),
                getPhotoFile().getPath()
        });

        parcel.writeParcelable(getLatLng(), 0);
    }

    protected Photo(Parcel in){
        super(in);

        String[] stringData   = new String[2];
        in.readStringArray(stringData);
        setDescription(stringData[0]);
        setPhotoFile(new File(stringData[1]));

        setLatLng(((LatLng) in.readParcelable(LatLng.class.getClassLoader())));
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public String getLabel() {
        return getPhotoFile().getAbsolutePath();
    }
}
