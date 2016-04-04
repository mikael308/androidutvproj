
package com.example.mikael.androidutvproj.eventlist;

import android.content.Context;
import android.widget.ListView;

import com.example.mikael.androidutvproj.event.Apartment;
import com.example.mikael.androidutvproj.event.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mikael Holmbom
 * @version 1.1
 * @see Event
 * @see EventListAdapter
 * @see EventListItem
 */
class EventListView extends ListView {

    public EventListView(Context context) {
        super(context);

    }

    public void setAdapter(EventListAdapter adapter) {

        //TODO add items from database
        Event e;
        for(int i = 1; i <= 25; i++){

            //TODO tmp Event
            e = new Event("fakestreet " + i);
            e.setDate(new Date());

            Apartment a =e.getApartment();
            a.setStartBid( i * 1000000);
            a.setLivingSpace(i * 11 / 2);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1950);
            Date constrYear = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            a.setConstructYear(constrYear);

            double floor = (double)(i+1) * 0.5;
            a.setFloor(floor);
            e.setDate(new Date());

            if (i < 3)
                a.setRent(i * 1_000);
            else
                a.setRent(i * 800);

            a.setDescription("here at fakestreet " + i + " everything is fake, but its cool man, recordless. its one of my selfdefense mechanisms");
            a.setType(Apartment.Type.CONDOMINIUM);
            a.setRooms(i+1);
            adapter.add(e);
        }

        adapter.notifyDataSetChanged();
        super.setAdapter(adapter);
    }

}
