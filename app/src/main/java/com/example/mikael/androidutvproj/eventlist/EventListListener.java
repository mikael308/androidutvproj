package com.example.mikael.androidutvproj.eventlist;

import com.example.mikael.androidutvproj.fragment.OnListItemClickListener;

/**
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @see EventListFragment
 */
public interface EventListListener extends OnListItemClickListener<EventListItem> {
    /**
     * called on action to create new event
     */
    void onNewEvent();


}
