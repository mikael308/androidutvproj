
package com.example.mikael.androidutvproj.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class Event extends ChildDataAccessObject implements Parcelable, Comparable<Event>{

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


    public Event(){
        super("event-"+RandomString.getRandomString(20));
    }
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

    /**
     * determine if this Event has past date
     * @return
     */
    public boolean isPast(){
        Date now = new Date();
        return getDate().before(now);
    }

    /**
     * determine if this Event date is today
     * @return true if Event is today
     */
    public boolean isToday(){
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR));

    }

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

        parcel.writeLong(getDate().getTime());

    }

    /**
     * constructor used for parcelable
     * @param in
     */
    protected Event(Parcel in){
        super(in);

        mDate = new Date(in.readLong());
    }



    /**
     * comparison is made on Events RealEstate address<br>
     *     comparison is not case-sensitive
     * @param another
     * @return
     */
    @Override
    public int compareTo(Event another) {

        return getId().toLowerCase().compareTo(another.getId().toLowerCase());
    }

    @Override
    public String toString(){
        return getDateString(DATEFORMAT_STDFORMAT);
    }

    @Override
    public String getLabel() {
        return getDateString(DATEFORMAT_STDFORMAT);
    }

    @Override
    public Event clone() {
        Event clone = new Event();
        clone.setId(getId());
        clone.setDate(getDate());

        return clone;
    }
}
