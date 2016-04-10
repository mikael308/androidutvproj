
package com.example.mikael.androidutvproj.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * class containing information about a Event
 *
 * 	<ul>
 * 	  <li>date of Event</li>
 * 	  <li>Apartment</li>
 * 	  <li>list of photos</li>
 * 	  <li>LatLngBounds with coverage of destination of all photo objects</li>
 * 	</ul>
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.0
 * @see Apartment
 * @see Photo
 */
public class Event implements Parcelable, Comparable<Event>{

    /**
     * apartment of this event
     */
    private Apartment mApartment;
	/**
	* collection of photos of this Event
	*/
    private Vector<Photo> mPhotos       = new Vector<Photo>();
	/**
	* bounds of map according to the LatLng of <Code>mPhotos</Code> instances
	* used to show a map viewing all LatLngs of <Code>mPhotos</Code>
	* @see Photo
	*/
    private LatLngBounds mMapBounds = new LatLngBounds(new LatLng(0,0), new LatLng(0,0));
	/**
	* date of this event
	*/
    private Date mDate;
	/**
	* standard date-time format used as standard
	* use with :
	*	getSDateString(String)
	*/
    public static final String DATEFORMAT_STDFORMAT = "yyyy-MM-dd HH:mm";

    /**
     * creates Event object without description, with date set to time object was created
     * @param address
     */
    public Event(String address){
        mApartment = new Apartment(address);
    }
    public Event(Apartment apartment){
        mApartment = apartment;
    }

    /**
     * get this apartment
     * @return this apartment
     */
    public Apartment getApartment(){
        return mApartment;
    }
    /**
     * gets this date
     * @return this date
     */
    public Date getDate(){
        return mDate;
    }
    /**
     * gets date in format <Code>dateformat</Code>
     * @param dateformat
     * @return this startdate in format <Code>dateformat</Code>
     */
    public String getDateString(String dateformat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        return sdf.format(getDate());
    }

    /**
     * gets size of this photo-vector
     * @return size of this photo-vector
     */
    public int getPhotosSize(){
        return mPhotos.size();
    }

    /**
     * add new photo to this photo container<br>
     * extends MapBounds if photo is outside of MapBounds
     * @param photo new photo to add
     */
    public void addPhoto(Photo photo){
        if(mPhotos.isEmpty()){
            mMapBounds = new LatLngBounds(photo.getLatLng(), photo.getLatLng());
        } else {
            mMapBounds = mMapBounds.including(photo.getLatLng());
        }

        mPhotos.add(photo);
    }

    /**
     * set this date
     * @param date new date
     */
    public void setDate(Date date){
        mDate = date;
    }

    /**
     * set this apartment
     * @param apartment new apartment
     */
    public void setApartment(Apartment apartment){
        mApartment = apartment;
    }

    ///////////////////////////////
    //     PARCELABLE METHODS
    ///////////////////////////////

    @Override
    public int hashCode(){
        return getApartment().hashCode() * (int) Math.pow(10, 32) + mDate.hashCode();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Event)
            return mApartment.getAddress().equals(((Event) o).getApartment().getAddress());

        return false;
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>(){
        public Event createFromParcel(Parcel in){
            return new Event(in);
        }
        public Event[] newArray(int size){
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeStringArray(new String[]{
                getApartment().getAddress(),
                getApartment().getDescription(),
                getApartment().getType()
        });
        parcel.writeLongArray(new long[]{
                mDate.getTime(),
                getApartment().getConstructYear().getTime()
        });
        parcel.writeIntArray(new int[]{
                getApartment().getStartBid(),
                getApartment().getRent()
        });
        parcel.writeDoubleArray(new double[]{
                getApartment().getFloor(),
                getApartment().getRooms(),
                getApartment().getLivingSpace()
        });

        //parcel.writeLongArray(dates);
    }

    /**
     * constructor used for parcelable
     * @param in
     */
    private Event(Parcel in){

        String[] stringData   = new String[3];
        in.readStringArray(stringData);
        Apartment apartment = new Apartment(stringData[0]);
        apartment.setDescription(stringData[1]);
        apartment.setType(stringData[2]);

        mApartment = apartment;
        long[] longData = new long[2];
        in.readLongArray(longData);
        mDate = new Date(longData[0]);
        getApartment().setConstructYear(new Date(longData[1]));

        int[] intData = new int[2];
        in.readIntArray(intData);
        getApartment().setStartBid(intData[0]);
        getApartment().setRent(intData[1]);

        double[] doubleData = new double[3];
        in.readDoubleArray(doubleData);
        getApartment().setFloor(doubleData[0]);
        getApartment().setRooms(doubleData[1]);
        getApartment().setLivingSpace(doubleData[2]);

    }

    /**
     * comparison is made on evnts Apartment address<br>
     *     comparison is not case-sensitive
     * @param another
     * @return
     */
    @Override
    public int compareTo(Event another) {

        return getApartment().getAddress().toLowerCase().compareTo(another.getApartment().getAddress().toLowerCase());
    }
    }
}
