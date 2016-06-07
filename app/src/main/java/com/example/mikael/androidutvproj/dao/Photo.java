

package com.example.mikael.androidutvproj.dao;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mikael.androidutvproj.PhotoFile;
import com.example.mikael.androidutvproj.tool.RandomString;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Date;

/**
 * @author Mikael Holmbom - miho1202
 * @version 1.0
 * class used for holding information about a Photo
 * containing information :
 *	- content description
 *	- destination of photo
 *	- date photo was taken
 */
public class Photo extends ChildDataAccessObject{


    /**
     * this photo file
     */
    private String mPhotoFilePath;

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
        super("photo-"+ RandomString.getRandomAlphaString(20));
    }

    public Photo(String id){
        super(id);
    }

    public Photo(String id, File photoFile) {
        super(id);
        mPhotoFilePath = photoFile.getAbsolutePath();
    }

    public Photo(File photoFile){
        super("photo-"+RandomString.getRandomAlphaString(20));
        mPhotoFilePath = photoFile.getAbsolutePath();
    }
    

    /**
     * std ctor
     * @param photofile file containing photo
     * @param description description of this photo
     * @param latlng destination of this photo
     * @param date date this photo was taken
     */
    public Photo(File photofile, String description, LatLng latlng, Date date){
        super("photo-"+RandomString.getRandomAlphaString(20));

        mPhotoFilePath  = photofile.getAbsolutePath();
        mDescription    = description;
        mLatLng         = latlng;
        mDate           = date;
    }


    /**
     * get this photofile
     * @return  photofile
     */
    public Bitmap getPhotoBitmap(){
        return BitmapFactory.decodeFile(mPhotoFilePath);
    }

    /**
     * get this photofile
     * @return  photofile
     */
    public PhotoFile getPhotoFile(){

        PhotoFile f = new PhotoFile(mPhotoFilePath);
        if(f.exists())
            return f;
        else
            return null;

    }

    public String getPhotoPath(){
        return mPhotoFilePath;
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
        mPhotoFilePath = f.getAbsolutePath();
        return this;
    }

    public Photo setPhotoFile(String filepath){
        mPhotoFilePath = filepath;
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
        return mPhotoFilePath;
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

        parcel.writeString(getDescription());
        if (getPhotoFile() != null){
            parcel.writeString(getPhotoFile().getAbsolutePath());
        }

        if(getDate() != null)
            parcel.writeLong(getDate().getTime());
        else
            parcel.writeLong(0);

        if (getLatLng() != null) {
            parcel.writeDouble(getLatLng().latitude);
            parcel.writeDouble(getLatLng().longitude);
        }

    }

    protected Photo(Parcel in){
        super(in);

        setDescription(in.readString());
        setPhotoFile(new File(in.readString()));

        long datetime = in.readLong();
        if (datetime > 0)
            setDate(new Date(datetime));

        double lat = in.readDouble();
        double lng = in.readDouble();
        setLatLng(new LatLng(lat, lng));

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
        if(getPhotoFile() != null)
            return getPhotoFile().getAbsolutePath();
        else
            return "nullphoto";
    }

    @Override
    public Photo clone() {
        Photo clone = new Photo(    getId())
                .setDescription(    getDescription())
                .setForeignKey(     getForeignKey());

        if(getDate() != null)
            clone.setDate(          getDate());
        if(getLatLng() != null)
            clone.setLatLng(        getLatLng());
        if(getPhotoFile() != null)
            clone.setPhotoFile(     getPhotoFile());

        return clone;
    }
}
