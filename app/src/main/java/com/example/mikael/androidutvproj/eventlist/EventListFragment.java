package com.example.mikael.androidutvproj.eventlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.mikael.androidutvproj.event.Event;

/**
 * fragment listing current Events<br>
 * contains :
 * <ul>
 *     <li>list of current events</li>
 * </ul>
 * for callback function on selecting listitems, use interface EventListFragment.OnArticleSelectedListener
 *
 * @author Mikael Holmbom
 * @version 1.1
 * @see OnArticleSelectedListener
 * @see Event
 */
public class EventListFragment extends ClickableListFragment<Event> {

    /**
     * this listview
     */
    private EventListView mListEvents;
    /**
     * this listadapter
     */
    private EventListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mListEvents = new EventListView(this.getActivity().getApplicationContext());
        mListEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = getAdapter().getItem(position);
                mListener.onArticleSelected(e);
            }
        });
        mListEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = getAdapter().getItem(position);
                mListener.onArticleLongClick(e);
                return true;
            }
        });

        mAdapter = new EventListAdapter(this.getActivity(), mListEvents.getId());
        mAdapter.setResources(getResources());
        mListEvents.setAdapter(mAdapter);


        return mListEvents;
    }

    public EventListAdapter getAdapter(){
        return mAdapter;
    }


}
