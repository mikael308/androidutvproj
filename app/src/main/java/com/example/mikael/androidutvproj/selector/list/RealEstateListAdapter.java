package com.example.mikael.androidutvproj.selector.list;


import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.settings.Settings;

import java.util.Collections;
import java.util.Comparator;

/**
 * Adapter used for ListView containing EventListItem
 * @author Mikael Holmbom
 * @version 1.1
 * @see RealEstateListItem
 */
public class RealEstateListAdapter extends BaseAdapter {


    private Context mContext;

    private int mColor_hasComingShows;
    private int mColor_hasNoComingShows;
    private int mColor_hasShowingToday;

    private boolean mShowColors = false;


    public RealEstateListAdapter(Context context) {
        mContext = context;

        mColor_hasComingShows       = ContextCompat.getColor(mContext, R.color.translucent20_green);
        mColor_hasNoComingShows     = ContextCompat.getColor(mContext, R.color.translucent20_yellow);
        mColor_hasShowingToday      = ContextCompat.getColor(mContext, R.color.translucent40_green);

        updateSettings();
    }

    @Override
    public int getCount() {
        try{
            return DataMapper.getRealEstateList().size();
        } catch(NullPointerException e){
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateSettings(){
        mShowColors = mContext
                .getSharedPreferences(Settings.SHAREDPREFKEY_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(Settings.SHAREDPREFKEY_SHOWDATES, false);

    }

    @Override
    public RealEstate getItem(int position) {
        return DataMapper.getRealEstateList().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RealEstate realEstate = getItem(position);
        RealEstateListItem item = new RealEstateListItem(mContext, realEstate);

        if(mShowColors) {
            if (realEstate.hasComingShowings()) {
                if (realEstate.hasShowingToday()) {
                    item.setBackgroundColor(mColor_hasShowingToday);
                } else {
                    item.setBackgroundColor(mColor_hasComingShows);
                }
            } else {
                item.setBackgroundColor(mColor_hasNoComingShows);
            }
        }

        return item;
    }


    /**
     * animatedRemove item from this adapter
     * @param realEstate item to animatedRemove
     *//*

    public void animatedRemove(RealEstate realEstate){
        animatedRemove(new RealEstateListItem(getContext(), realEstate));
    }


    public void animatedRemove(RealEstate realEstate){

        final RealEstateListItem animItem = getItem(getPosition(realEstate));

        Animators.anim_remove(getContext(), animItem)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        animItem.onFeedbackPost(new Runnable() {
                            public void run() {
                                remove(animItem);
                            }
                        });
                    }
                });


    }

    */


}
