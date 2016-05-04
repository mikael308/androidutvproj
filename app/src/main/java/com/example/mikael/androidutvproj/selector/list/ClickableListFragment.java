package com.example.mikael.androidutvproj.selector.list;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 *
 * ListFragment with callback functions according to OnListItemClickListener
 * @author Mikael Holmbom
 * @version 1.0
 * @param <ApartmentListItem> type of item stored in this list
 * @see OnListItemClickListener
 */
public abstract class ClickableListFragment<ApartmentListItem> extends ListFragment {

    protected OnListItemClickListener<ApartmentListItem> mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            if (context instanceof Activity)
                mListener = (OnListItemClickListener) context;

        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + OnListItemClickListener.class);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApartmentListItem item = (ApartmentListItem) view;
                mListener.onItemClick(item, position);
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ApartmentListItem item = (ApartmentListItem) view;
                mListener.onItemLongClick(item, position);
                return true;
            }
        });
    }
}
