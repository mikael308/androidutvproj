package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.example.mikael.androidutvproj.R;

/**
 * ListItem using content from {@link SettingsListItem} and add subheader info: {@link #setInfo(String)}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class SettingsListInfoItem extends SettingsListItem {

    /**
     * settings listitem info
     */
    private TextView mInfo;


    public SettingsListInfoItem(Context context) {
        super(context);
    }

    public SettingsListInfoItem(Context context, String header) {
        super(context, header);
    }

    public SettingsListInfoItem(Context context, String header, String info){
        super(context, header);

        mInfo.setText(info);
    }

    @Override
    protected void init() {
        super.init();

        mInfo = new TextView(getContext());

        mInfo.setTextColor(Color.BLACK);
        mInfo.setPadding(getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_left), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_top), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_right), getResources().getDimensionPixelSize(R.dimen.settings_listitem_info_padding_bottom));
        mInfo.setTextColor(Color.GRAY);

        addView(mInfo);
    }

    /**
     * get this info string
     * @return info text
     */
    public String getInfo(){
        return mInfo.getText().toString();
    }

    /**
     * set this info text string
     * @param newInfo new text value
     * @return this SettingsListItem
     */
    public SettingsListItem setInfo(String newInfo){
        mInfo.setText(newInfo);
        return this;
    }

}
