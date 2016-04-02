package com.example.mikael.androidutvproj.listEvents;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.event.Event;

/**
 * @author Mikael Holmbom
 * @version 1.0
 * @see Event
 */
public class ListEventsAdapter extends ArrayAdapter<Event>  {

    public ListEventsAdapter(Context context, int resource) {
        super(context, resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListEventItem item;
        if(convertView == null){
            item = new ListEventItem(getContext());

            item.setLayoutParams(new ListView.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
            ));
            int padd = getContext().getResources().getDimensionPixelSize(R.dimen.eventlistitem_padding);
            item.setPadding(padd, padd, padd, padd);
            item.setInnerPadding(padd, getContext().getResources().getDimensionPixelSize(R.dimen.eventlistitem_spacing));

        } else {
            item = (ListEventItem) convertView;
        }

        Event event = getItem(position);
        item.setName(           event.getApartment().getAddress());
        item.setDescription(    event.getApartment().getDescription());


        return item;
    }
}
