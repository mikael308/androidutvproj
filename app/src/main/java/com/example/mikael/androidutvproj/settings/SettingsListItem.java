package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * list item used in SettingsListAdapter<br>
 *     contains:
 *<ul>
 *     <li>header</li>
 *</ul>
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @see SettingsListAdapter
 * @see com.example.mikael.androidutvproj.activity.SettingsActivity
 */
class SettingsListItem extends LinearLayout {
    /**
     * settings listitem header
     */
    private TextView mHeader;


    public SettingsListItem(Context context){
        super(context);
        init();
    }
    public SettingsListItem(Context context, String header){
        super(context);
        init();

        mHeader.setText(header);
    }

    /**
     * initialize this object
     */
    protected void init(){
        setOrientation(VERTICAL);

        mHeader = new TextView(getContext());
        addView(mHeader);
    }

    /**
     * get this header string
     * @return header text
     */
    public String getHeader(){
        return mHeader.getText().toString();
    }

    /**
     * set this header text string
     * @param newHeader new text value
     * @return this SettingsListItem
     */
    public SettingsListItem setHeader(String newHeader){
        mHeader.setText(newHeader);
        return this;
    }

}
