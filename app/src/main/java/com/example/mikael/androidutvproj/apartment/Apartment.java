package com.example.mikael.androidutvproj.apartment;

import android.os.Parcel;

import com.example.mikael.androidutvproj.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Apartment
 * @author Mikael Holmbom
 * @version 1.0
 */
public class Apartment extends DataAccessObject implements Comparable<Apartment>{

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

    private ArrayList<Event> mShowings = new ArrayList<>(2);

    /**
     * collection of photos of this Event
     */
    private ArrayList<Photo> mPhotos       = new ArrayList<>();

    /**
     * create Apartment
     * @param id id
     */
    public Apartment(String id){
        super(id);
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

    public ArrayList<Event> getShowings(){
        return mShowings;
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

    public void setConstructYear(long time){
        mConstructYear = new Date(time);
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

    @Override
    public boolean equals(Object o) {
        if( o instanceof Apartment)
            return getId().equals(((Apartment) o).getId());
        return false;
    }

    @Override
    public int compareTo(Apartment another) {
        return getAddress().compareTo(another.getAddress());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);

        parcel.writeStringArray(new String[]{
                getId(),
                getAddress(),
                getDescription(),
                getType().name() //TODO denna ger error om du gåpr ur app i eventlist view
        });
        parcel.writeLongArray(new long[]{
                getConstructYear().getTime()
        });
        parcel.writeIntArray(new int[]{
                getStartBid(),
                getRent()
        });
        parcel.writeDoubleArray(new double[]{
                getFloor(),
                getRooms(),
                getLivingSpace()
        });
    }

    protected Apartment(Parcel in){
        super(in);

        String[] stringData   = new String[4];
        in.readStringArray(stringData);
        setDescription(stringData[2]);
        setType(Apartment.Type.valueOf(stringData[3])); //TODO tmp use enum functions

        long[] longData = new long[1];
        in.readLongArray(longData);
        setConstructYear(new Date(longData[1]));

        int[] intData = new int[2];
        in.readIntArray(intData);
        setStartBid(intData[0]);
        setRent(intData[1]);

        double[] doubleData = new double[3];
        in.readDoubleArray(doubleData);
        setFloor(doubleData[0]);
        setRooms(doubleData[1]);
        setLivingSpace(doubleData[2]);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
