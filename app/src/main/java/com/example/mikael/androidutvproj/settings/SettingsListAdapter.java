
package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mikael.androidutvproj.R;

/**
 * ListAdapter containing SettingsListItem<br>
 * used in SettingsActivity
 * @author Mikael Holmbom
 * @version 1.0
 * @see com.example.mikael.androidutvproj.activity.SettingsActivity
 * @see SettingsListItem
 */
class SettingsListAdapter extends ArrayAdapter<SettingsListItem> {


    public SettingsListAdapter(Context context, int resource) {
        super(context, resource);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SettingsListItem listitem = getItem(position);

        int padd    = getContext().getResources().getDimensionPixelSize(R.dimen.settings_listitem_padding);
        listitem    .setPadding(padd, padd, padd, padd);

        return listitem;
    }
}
