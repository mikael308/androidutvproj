package com.example.mikael.androidutvproj.eventlist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mikael.androidutvproj.event.Event;


/**
 * fragment listing current Events<br>
 * contains :
 * <ul>
 *     <li>list of current events</li>
 * </ul>
 * for callback function on selecting listitems, use interface OnListItemClickListener
 *
 * @author Mikael Holmbom
 * @version 1.1
 * @see OnListItemClickListener
 * @see Event
 */
public class EventListFragment extends ClickableListFragment<EventListItem> {

    /**
     * activity using this fragment
     */
    protected EventListListener mEventListListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            if (context instanceof Activity)
                mEventListListener = (EventListListener) context;

        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + EventListListener.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        ListView listView = new ListView(getActivity());
        EventListAdapter adapter = new EventListAdapter(getActivity(), listView.getId());

        setListAdapter(adapter);

        return listView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
