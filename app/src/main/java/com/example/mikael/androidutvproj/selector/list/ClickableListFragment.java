package com.example.mikael.androidutvproj.selector.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import com.example.mikael.androidutvproj.Observer;


/**
 *
 * ListFragment with callback functions according to OnListItemClickListener
 * @author Mikael Holmbom
 * @version 1.0
 * @param <ListItem> type of item stored in this list
 * @see OnListItemClickListener
 */
public abstract class ClickableListFragment<ListItem> extends ListFragment implements Observer {


    public interface OnListItemClickListener<T>{
        /**
         * on listitem click
         * @param selectedItem selected item
         */
        void onItemClick(T selectedItem, int position);
        /**
         * on listitem long click
         * @param selectedItem selected item
         */
        void onItemLongClick(T selectedItem, int position);
    }


    protected OnListItemClickListener<ListItem> mListener;

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
                ListItem item = (ListItem) view;
                mListener.onItemClick(item, position);
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) view;
                mListener.onItemLongClick(item, position);
                return true;
            }
        });
    }


}
