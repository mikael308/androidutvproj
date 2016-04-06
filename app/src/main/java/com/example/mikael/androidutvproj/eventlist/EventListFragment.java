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


        mAdapter = new EventListAdapter(this.getActivity(), mListEvents.getId());
        mAdapter.setResources(getResources());
        mListEvents.setAdapter(mAdapter);


        return mListEvents;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
