package com.example.mikael.androidutvproj.dao;

import android.os.Parcel;

/**
 * database entity with parent-child relation to another DataAccessObject described as {@link #mForeignKey} (parent)
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class ChildDataAccessObject extends DataAccessObject {


    /**
     * id of this parent instance: foreign key
     */
    private String mForeignKey = null;

    /**
     *
     * @param id this id
     */
    public ChildDataAccessObject(String id) {
        super(id);
    }

    /**
     *
     * @param id this id
     * @param mForeignKey id of parent DataAccessObject
     */
    public ChildDataAccessObject(String id, String mForeignKey) {
        super(id);
        this.mForeignKey = mForeignKey;
    }

    /**
     * get this foreign key
     * @return
     */
    public String getForeignKey(){
        return mForeignKey;
    }

    /**
     * set this foreign key
     * @param foreignKey
     */
    public void setForeignKey(String foreignKey){
        mForeignKey = foreignKey;
    }


    public ChildDataAccessObject(Parcel in) {
        super(in);
        setForeignKey(in.readString());
    }

    @Override
    public abstract String getLabel();

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(getForeignKey());
    }
}
