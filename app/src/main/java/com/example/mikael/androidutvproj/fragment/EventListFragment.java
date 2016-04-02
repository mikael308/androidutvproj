package com.example.mikael.androidutvproj.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.event.Event;
import com.example.mikael.androidutvproj.listEvents.ListEventsAdapter;
import com.example.mikael.androidutvproj.listEvents.ListEventsView;

/**
 * fragment listing current Events<br>
 * contains :
 * <ul>
 *     <li>list of current events</li>
 * </ul>
 * for callback function on selecting listitems, use interface EventListFragment.OnArticleSelectedListener
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @see EventListFragment.OnArticleSelectedListener
 */
public class EventListFragment extends Fragment {

    /**
     * interface used for callback function when using EventListFragment
     */
    public interface OnArticleSelectedListener{
        public void onArticleSelected(Event selectedEvent);

        /**
         * on listitem long click
         * @param selectedEvent
         */
        public void onArticleLongClick(Event selectedEvent);
    }

    private ListEventsView mListEvents;
    private ListEventsAdapter mAdapter;

    private OnArticleSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mListener = (OnArticleSelectedListener) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement " + OnArticleSelectedListener.class);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_empty, container, false);


        mListEvents = new ListEventsView(this.getActivity().getApplicationContext());
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

        mAdapter = new ListEventsAdapter(this.getActivity(), mListEvents.getId());
        mListEvents.setAdapter(mAdapter);

        // add views to root
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout_empty);
        layout.addView(mListEvents);

        return rootView;

    }

    public ListEventsAdapter getAdapter(){
        return mAdapter;
    }


}
