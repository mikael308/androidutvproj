package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikael.androidutvproj.R;

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
        this.setOrientation(VERTICAL);

        LinearLayout headerLayout = new LinearLayout(getContext());
        headerLayout.setOrientation(HORIZONTAL);

        mHeader = new TextView(getContext());
        mHeader.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        //TODO fixa så header blir MEDIUM TEXTVIEW
        //mHeader.setTextSize(getResources().getDimension(R.dimen.settings_listitem_header_textsize));


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
