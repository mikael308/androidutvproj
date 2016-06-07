package com.example.mikael.androidutvproj.dao;

import android.os.Parcel;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.tool.RandomString;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * //TODO denna behöver inte id?? gör composition med eventID+address
 *
 * RealEstate
 * @author Mikael Holmbom
 * @version 1.0
 */
public class RealEstate extends ParentDataAccessObject implements Comparable<RealEstate>{

    /**
     * Type of RealEstate
     */
    public enum Type{
        CONDOMINIUM     (0, R.string.realestate_type_condominium),
        HOUSE           (1, R.string.realestate_type_house),
        TOWNHOUSE       (2, R.string.realestate_type_townhouse),
        HOLIDAY_HOUSE   (3, R.string.realestate_type_holidayhouse),
        GROUND_PLOT     (4, R.string.realestate_type_ground_plot),
        WOODLAND_SITE   (5, R.string.realestate_type_woodland_site),
        OTHER           (6, R.string.realestate_type_other);

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
        public static Type newType(int id){
            for(Type t : Type.values())
                if(t.getId() == id) return t;
            return null;
        }
        //TODO ta bort id och använd bara nameid???
    }

    /**
     * this address
     */
    private String mAddress;
    /**
     * this position
     */
    private LatLng mLatLng;
    /**
     * this startbid
     */
    private int mStartBid;
    /**
     * this description
     */
    private String mDescription;
    /**
     * this livingspace
     */
    private double mLivingSpace;
    /**
     * this rent
     */
    private int mRent;
    /**
     * this amount of rooms
     */
    private double mRooms;
    /**
     * this year of construction
     */
    private Date mConstructYear;
    /**
     * this floor number
     */
    private double mFloor;
    /**
     * this type of RealEstate
     */
    private Type mType;
    /**
     * list of showings
     */
    private ArrayList<Event> mShowings = new ArrayList<>(1);

    /**
     * collection of photos of this Event
     */
    private ArrayList<Photo> mPhotos       = new ArrayList<>(0);

    public RealEstate(){
        super("realestate-" + RandomString.getRandomAlphaString(20));
    }
    /**
     * create RealEstate
     * @param id id
     */
    public RealEstate(String id){
        super(id);
    }
    /**
     * get this title
     * @return this title
     */
    public String getAddress(){
        return mAddress;
    }

    public LatLng getLatLng(){
        return mLatLng;
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

    public List<Photo> getPhotos(){
        return mPhotos;
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
     * get this price per livingspace<br>
     *     that is:  startbid / livingspace
     * @return
     */
    public double getPriceLivingSpace(){
        return getStartBid() / getLivingSpace();
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
    public RealEstate setAddress(String address){
        mAddress = address;
        return this;
    }
    public RealEstate setLatLng(LatLng latlng){
        mLatLng = latlng;
        return this;
    }
    /**
     *
     * @param startBid new startbid
     */
    public RealEstate setStartBid(int startBid){
        mStartBid = startBid;
        return this;
    }

    /**
     * set this year of construction
     * @param constructYear
     */
    public RealEstate setConstructYear(Date constructYear){
        mConstructYear = constructYear;
        return this;
    }

    public RealEstate setConstructYear(long time){
        mConstructYear = new Date(time);
        return this;
    }
    /**
     * set this livingspace
     * @param livingSpace new livingspace value
     */
    public RealEstate setLivingSpace(double livingSpace){
        mLivingSpace = livingSpace;
        return this;
    }
    /**
     * set this rent
     * @param rent new rent value
     */
    public RealEstate setRent(int rent){
        mRent = rent;
        return this;
    }
    /**
     * sets this description
     * @param description new description
     */
    public RealEstate setDescription(String description){
        mDescription = description;
        return this;
    }
    /**
     * set this type
     * @param type new type value
     */
    public RealEstate setType(Type type){
        mType = type;
        return this;
    }
    /**
     * set this amount of room
     * @param rooms new amount of rooms
     */
    public RealEstate setRooms(double rooms){
        mRooms = rooms;
        return this;
    }
    /**
     * set this floor
     * @param floor new floor value
     */
    public RealEstate setFloor(double floor){
        mFloor = floor;
        return this;
    }

    /**
     * determines if this RealEstate has showings today
     * @return true if >= 1 showing is today
     */
    public boolean hasShowingToday(){
        for(Event showing : mShowings)
            if(showing.isToday() && ! showing.isPast()) return true;
        return false;
    }

    public boolean hasComingShowings(){
        boolean futureshowings = false;
        for(Event showing : mShowings){
            if(showing.isProspective()) futureshowings = true;
        }
        return futureshowings;
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
        if( o instanceof RealEstate)
            return getId().equals(((RealEstate) o).getId());
        return false;
    }

    @Override
    public int compareTo(RealEstate another) {
        return getAddress().compareTo(another.getAddress());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);

        parcel.writeStringArray(new String[]{
                getId(),
                getAddress(),
                getDescription()
        });
        parcel.writeLongArray(new long[]{
                getConstructYear().getTime()
        });
        parcel.writeIntArray(new int[]{
                getStartBid(),
                getRent(),
                getType().getId()
        });
        parcel.writeDoubleArray(new double[]{
                getFloor(),
                getRooms(),
                getLivingSpace()
        });

        parcel.writeParcelableArray((Event[]) getShowings().toArray(), 0); //TODO kolla upp, vad är åparcelableflag??? 0..
        parcel.writeParcelable(getLatLng(), 0);
    }

    protected RealEstate(Parcel in){
        super(in);

        String[] stringData   = new String[4];
        in.readStringArray(stringData);
        setAddress(stringData[1]);
        setDescription(stringData[2]);

        long[] longData = new long[1];
        in.readLongArray(longData);
        setConstructYear(new Date(longData[1]));

        int[] intData = new int[3];
        in.readIntArray(intData);
        setStartBid(intData[0]);
        setRent(intData[1]);
        setType(Type.newType(intData[2]));

        double[] doubleData = new double[3];
        in.readDoubleArray(doubleData);
        setFloor(doubleData[0]);
        setRooms(doubleData[1]);
        setLivingSpace(doubleData[2]);

        for(Event showing : (Event[]) in.readParcelableArray(Event.class.getClassLoader())){
            getShowings().add(showing);
        }

        setLatLng(((LatLng) in.readParcelable(LatLng.class.getClassLoader())));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getLabel() {
        return getAddress() + ", " + getFloor();
    }
}
