
package com.example.mikael.androidutvproj.apartment;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class Event extends DataAccessObject implements Parcelable, Comparable<Event>{


/*    *//**
     * apartment of this event
     *//*
    private Apartment mApartment;
	*//**
	* collection of photos of this Event
	*//*
    private Vector<Photo> mPhotos       = new Vector<Photo>();*/
	/**
	* bounds of map according to the LatLng of <Code>mPhotos</Code> instances
	* used to show a map viewing all LatLngs of <Code>mPhotos</Code>
	* @see Photo
	*/
    //private LatLngBounds mMapBounds = new LatLngBounds(new LatLng(0,0), new LatLng(0,0));
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


    public Event(String id){
        super(id);
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
/*
TODO -..
    */
/**
     * add new photo to this photo container<br>
     * extends MapBounds if photo is outside of MapBounds
     * @param photo new photo to add
     *//*

    public void addPhoto(Photo photo){
        if(mPhotos.isEmpty()){
            mMapBounds = new LatLngBounds(photo.getLatLng(), photo.getLatLng());
        } else {
            mMapBounds = mMapBounds.including(photo.getLatLng());
        }

        mPhotos.add(photo);
    }
*/

    /**
     * set this date
     * @param date new date
     */
    public void setDate(Date date){
        mDate = date;
    }


    @Override
    public int hashCode(){
        return getId().hashCode();
        //return getApartment().hashCode() * (int) Math.pow(10, 32) + mDate.hashCode();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Event)
            return getId().equals(((Event) o).getId());

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
        super.writeToParcel(parcel, i);

        //parcel.writeParcelable(getApartment(), i);

        parcel.writeLongArray(new long[]{
                getDate().getTime()
        });

        /*
        parcel.writeStringArray(new String[]{
                getApartment().getAddress(),
                getApartment().getDescription(),
                getApartment().getType().name() //TODO denna ger error om du gåpr ur app i eventlist view
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
        */

        //parcel.writeLongArray(dates);
    }

    /**
     * constructor used for parcelable
     * @param in
     */
    protected Event(Parcel in){
        super(in);

        Apartment apartment =  in.readParcelable(Apartment.class.getClassLoader());

        long[] longData = new long[1];
        in.readLongArray(longData);
        mDate = new Date(longData[0]);


/*
        String[] stringData   = new String[4];
        in.readStringArray(stringData);
        Apartment apartment = new Apartment(stringData[1]);
        apartment.setDescription(stringData[2]);
        apartment.setType(Apartment.Type.valueOf(stringData[3])); //TODO tmp use enum functions

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
        getApartment().setLivingSpace(doubleData[2]);*/

    }

    /**
     * comparison is made on evnts Apartment address<br>
     *     comparison is not case-sensitive
     * @param another
     * @return
     */
    @Override
    public int compareTo(Event another) {

        return getId().compareTo(another.getId());
    }

    @Override
    public String toString(){
        return getId();
    }

}
