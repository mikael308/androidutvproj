
package com.example.mikael.androidutvproj.dao;

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
 * 	  <li>RealEstate</li>
 * 	  <li>list of photos</li>
 * 	  <li>LatLngBounds with coverage of destination of all photo objects</li>
 * 	</ul>
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.0
 * @see RealEstate
 * @see Photo
 */
public class Event extends DataAccessObject implements Parcelable, Comparable<Event>{

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

    public boolean isProspective(){
        Date now = new Date();
        return getDate().after(now);
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
    public Event setDate(Date date){
        mDate = date;
        return this;
    }


    @Override
    public int hashCode(){
        return getId().hashCode();
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

        parcel.writeLongArray(new long[]{
                getDate().getTime()
        });

    }

    /**
     * constructor used for parcelable
     * @param in
     */
    protected Event(Parcel in){
        super(in);

        RealEstate realEstate =  in.readParcelable(RealEstate.class.getClassLoader());

        long[] longData = new long[1];
        in.readLongArray(longData);
        mDate = new Date(longData[0]);

    }

    /**
     * comparison is made on evnts RealEstate address<br>
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
