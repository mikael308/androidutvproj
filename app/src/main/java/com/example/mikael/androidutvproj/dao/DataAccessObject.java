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
        setId(in.readString());

    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DataAccessObject)
            return getId().equals(((DataAccessObject) o).getId());

        return false;
    }

    /**
     * get a label describing this instance
     * @return
     */
    public abstract String getLabel();

}
