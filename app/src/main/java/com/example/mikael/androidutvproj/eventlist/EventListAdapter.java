package com.example.mikael.androidutvproj.eventlist;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mikael.androidutvproj.event.Event;

/**
 * Adapter used for ListView containing EventListItem
 * @author Mikael Holmbom
 * @version 1.1
 * @see EventListItem
 */
public class EventListAdapter extends ArrayAdapter<EventListItem> {

    public EventListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventListItem item = getItem(position);

        return item;
    }

    /**
     * add Event to this adapter
     * @param e Event to add
     */
    public void add(Event e){
        super.add(new EventListItem(getContext(), e));
    }

    /**
     * remove Event from this adapter
     * @param e Event to remove
     */
    public void remove(Event e){
        super.remove(new EventListItem(getContext(), e));
    }

    /**
     * get position of Event in this adapter
     * @param e Event to determine position in this adapter
     */
    public int getPosition(Event e){
        return super.getPosition(new EventListItem(getContext(), e));
    }
}
