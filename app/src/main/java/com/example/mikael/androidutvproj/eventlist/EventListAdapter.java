package com.example.mikael.androidutvproj.eventlist;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mikael.androidutvproj.event.Event;

/**
 * @author Mikael Holmbom
 * @version 1.1
 * @see Event
 */
public class EventListAdapter extends ArrayAdapter<Event>  {

    public EventListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventListItem item;
        if(convertView == null){
            item = new EventListItem(getContext());

            item.setLayoutParams(new ListView.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
            ));
            int padd = getContext().getResources().getDimensionPixelSize(R.dimen.eventlistitem_padding);
            item.setPadding(padd, padd/2, padd, padd/2);
            item.setInnerPadding(padd, getContext().getResources().getDimensionPixelSize(R.dimen.eventlistitem_spacing));

        } else {
            item = (EventListItem) convertView;
        }

        Event event = getItem(position);
        String name = event.getApartment().getAddress() + ", " + event.getApartment().getFloor();
        if(mResources != null)
            name += mResources.getString(R.string.apartment_floor_unit);

        item.setName(name);
        item.setDescription(    event.getApartment().getDescription());


        return item;
    }
}
