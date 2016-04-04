package com.example.mikael.androidutvproj.event;

import com.example.mikael.androidutvproj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Apartment
 * @author Mikael Holmbom
 * @version 1.0
 */
public class Apartment {

    public enum Type{
        CONDOMINIUM     (0, R.string.apartment_type_condominium),
        HOUSE           (1, R.string.apartment_type_house),
        TOWNHOUSE       (2, R.string.apartment_type_townhouse),
        HOLIDAY_HOUSE   (3, R.string.apartment_type_holidayhouse),
        GROUND_PLOT     (4, R.string.apartment_type_ground_plot),
        WOODLAND_SITE   (5, R.string.apartment_type_woodland_site),
        OTHER           (6, R.string.apartment_type_other);

        private int mNameId;
        private int mId;

        Type(int id, int nameId){
            mId = id;
            mNameId = nameId;
        }
        public int getNameId(){
            return mNameId;
        }
        public int getId(){
            return mId;
        }
    }

    /**
     * this address
     */
    private String mAddress;
    /**
     * this startbid
     */
    private int mStartBid = 0;
    /**
     * this description
     */
    private String mDescription = "";
    /**
     * this livingspace
     */
    private double mLivingSpace = 0.0;
    /**
     * this rent
     */
    private int mRent = 0;
    /**
     * this amount of rooms
     */
    private double mRooms = 0.0;
    /**
     * this year of construction
     */
    private Date mConstructYear;
    /**
     * this floor number
     */
    private double mFloor = 0.0;
    /**
     * this type of Apartment
     */
    private Type mType;

    /**
     * create Apartment with address
     * @param address address value
     */
    public Apartment(String address){
        mAddress = address;
    }
    /**
     * get this title
     * @return this title
     */
    public String getAddress(){
        return mAddress;
    }

    /**
     * get this year of construction as String
     * @return year of construction as String
     */
    public String getConstructYearString(){
        if(mConstructYear == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(mConstructYear);
    }

    /**
     * get this year of construction
     * @return year of construction
     */
    public Date getConstructYear(){
        return mConstructYear;
    }
    /**
     *
     * @return this startbid
     */
    public int getStartBid(){   return mStartBid;    }
    /**
     * gets this description
     * @return this description
     */
    public String getDescription(){
        return mDescription;
    }
    /**
     * get this livingspace
     * @return this livingspae
     */
    public double getLivingSpace(){
        return mLivingSpace;
    }
    /**
     * get this rent
     * @return this rent
     */
    public int getRent(){
        return mRent;
    }
    /**
     * get this type
     * @return this type
     */
    public Type getType(){
        return mType;

    }
    /**
     * get this amount of rooms
     * @return amount of rooms
     */
    public double getRooms(){
        return mRooms;
    }

    /**
     * get this floor
     * @return this floor
     */
    public double getFloor(){
        return mFloor;
    }
    /**
     * set this address
     * @param address new address
     */
    public void setAddress(String address){
        mAddress = address;
    }
    /**
     *
     * @param startBid new startbid
     */
    public void setStartBid(int startBid){mStartBid = startBid;}

    /**
     * set this year of construction
     * @param constructYear
     */
    public void setConstructYear(Date constructYear){
        mConstructYear = constructYear;
    }
    /**
     * set this livingspace
     * @param livingSpace new livingspace value
     */
    public void setLivingSpace(double livingSpace){mLivingSpace = livingSpace;}
    /**
     * set this rent
     * @param rent new rent value
     */
    public void setRent(int rent){
        mRent = rent;
    }
    /**
     * sets this description
     * @param description new description
     */
    public void setDescription(String description){
        mDescription = description;
    }
    /**
     * set this type
     * @param type new type value
     */
    public void setType(Type type){
        mType = type;
    }
    /**
     * set this amount of room
     * @param rooms new amount of rooms
     */
    public void setRooms(double rooms){
        mRooms = rooms;
    }
    /**
     * set this floor
     * @param floor new floor value
     */
    public void setFloor(double floor){
        mFloor = floor;
    }

    @Override
    public int hashCode(){
        return mAddress.hashCode();
    }

    @Override
    public String toString() {
        return getAddress() + ":" + getFloor();
    }
}
