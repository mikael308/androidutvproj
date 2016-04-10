package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikael.androidutvproj.R;

/**
 * list item used in SettingsListAdapter<br>
 *     contains:
 *<ul>
 *     <li>header</li>
 *     <li>info</li>
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
    /**
     * settings listitem info
     */
    private TextView mInfo;

    public SettingsListItem(Context context){
        super(context);
        mHeader = new TextView(getContext());
        mInfo = new TextView(getContext());

        init();
    }
    public SettingsListItem(Context context, String header){
        super(context);
        mHeader = new TextView(getContext());
        mHeader.setText(header);
        mInfo = new TextView(getContext());

        init();
    }
    public SettingsListItem(Context context, String header, String info) {
        super(context);
        mHeader = new TextView(getContext());
        mHeader.setText(header);
        mInfo = new TextView(getContext());
        mInfo.setText(info);

        init();
    }

    /**
     * initialize this object
     */
    private void init(){
        this.setOrientation(VERTICAL);

        LinearLayout headerLayout = new LinearLayout(getContext());
        headerLayout.setOrientation(HORIZONTAL);

        mHeader.setTextSize(getResources().getDimension(R.dimen.settings_listitem_header_textsize));
        mInfo.setTextColor(Color.BLACK);
        mInfo.setTextSize(getResources().getDimension(R.dimen.settings_listitem_info_textsize));
        mInfo.setPadding(getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_left), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_top), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_right), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_bottom));
        mInfo.setTextColor(Color.GRAY);

        addView(mHeader);
        addView(mInfo);
    }

    /**
     * get this header string
     * @return header text
     */
    public String getHeader(){
        return mHeader.getText().toString();
    }
    /**
     * get this info string
     * @return info text
     */
    public String getInfo(){
        return mInfo.getText().toString();
    }
    /**
     * set this header text string
     * @param newHeader new text value
     */
    public void setHeader(String newHeader){
        mHeader.setText(newHeader);
    }
    /**
     * set this info text string
     * @param newInfo new text value
     */
    public void setInfo(String newInfo){
        mInfo.setText(newInfo);
    }

}
