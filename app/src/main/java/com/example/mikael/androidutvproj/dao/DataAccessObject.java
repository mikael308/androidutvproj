package com.example.mikael.androidutvproj.dao;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class DataAccessObject implements Parcelable {

    /**
     * this id<br>
     *     used as key in database
     */
    private String mId;


    public DataAccessObject(String id){
        mId = id;
    }

    public String getId(){
        return mId;
    }
    protected void setId(String id){
        mId = id;
    }

    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(getId());
    }

    protected DataAccessObject(Parcel in){
        String[] stringData   = new String[0];
        in.readStringArray(stringData);
        setId(stringData[0]);

    }

}
