package com.example.mikael.androidutvproj.dao;

import android.os.Parcel;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class ParentDataAccessObject extends DataAccessObject {

    public ParentDataAccessObject(String id) {
        super(id);
    }

    public ParentDataAccessObject(Parcel in) {
        super(in);
    }

}
