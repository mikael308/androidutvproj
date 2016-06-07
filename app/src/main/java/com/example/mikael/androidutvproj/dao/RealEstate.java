package com.example.mikael.androidutvproj.dao;

import android.os.Parcel;
import android.os.Parcelable;

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
        for(Event showing : mShowings){
            if (! showing.isPast()) return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return mAddress.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getAddress() == null? "N/A": getAddress())
                .append(":")
                .append(getFloor() == Double.MIN_VALUE? "N/A":getFloor());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof RealEstate)
            return getId().equals(((RealEstate) o).getId());
        return false;
    }

    @Override
    public int compareTo(RealEstate another) {
        return getAddress().toLowerCase().compareTo(another.getAddress().toLowerCase());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);

        parcel.writeString(getAddress());
        parcel.writeString(getDescription());

        Date constrYear = getConstructYear();
        if (constrYear != null)
            parcel.writeLong(constrYear.getTime());

        parcel.writeInt(getStartBid());
        parcel.writeInt(getRent());
        parcel.writeInt(getType().getId());

        parcel.writeDouble(getFloor());
        parcel.writeDouble(getRooms());
        parcel.writeDouble(getLivingSpace());

        Event[] showings = new Event[getShowings().size()];
        for(int j = 0; j < getShowings().size(); j++)
            showings[j] = getShowings().get(j);

        parcel.writeTypedArray(showings, 0);

        parcel.writeParcelable(getLatLng(), PARCELABLE_WRITE_RETURN_VALUE);
    }

    protected RealEstate(Parcel in){
        super(in);

        setAddress(in.readString());
        setDescription(in.readString());

        setConstructYear(in.readLong());

        setStartBid(in.readInt());
        setRent(in.readInt());
        setType(Type.newType(in.readInt()));

        setFloor(in.readDouble());
        setRooms(in.readDouble());
        setLivingSpace(in.readDouble());

        Event[] showingArr = in.createTypedArray(Event.CREATOR);
        for (Event showing : showingArr) {
            getShowings().add(showing);
        }

        LatLng latLng = in.readParcelable(LatLng.class.getClassLoader());
        setLatLng(latLng);


    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RealEstate createFromParcel(Parcel in) {
            return new RealEstate(in);
        }

        public RealEstate[] newArray(int size) {
            return new RealEstate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getLabel() {
        return getAddress() + ", " + getFloor();
    }

    @Override
    public RealEstate clone() {
        RealEstate clone = new RealEstate(getId())
                .setAddress(getAddress())
                .setType(getType())
                .setConstructYear(getConstructYear())
                .setStartBid(getStartBid())
                .setRent(getRent())
                .setRooms(getRooms())
                .setLivingSpace(getLivingSpace())
                .setDescription(getDescription());

        clone.getShowings().addAll(     getShowings());
        clone.getPhotos().addAll(       getPhotos());

        return clone;
    }
}
