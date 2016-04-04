package com.example.mikael.androidutvproj.settings;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mikael.androidutvproj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings used for this app<br>
 * settings values stored in shared preferences
 * @author Mikael Holmbom
 * @version 1.0
 * @see SettingsListAdapter
 * @see SettingsListItem
 */
public class SettingsListFragment extends ListFragment {

    /**
     * shared preferenes main key : settings
     */
    public static final String SHAREDPREFKEY_SETTINGS = "SETTINGS";

    /**
     * this listview
     */
    private ListView                mListView;
    /**
     * this listadapter
     */
    private SettingsListAdapter     mListAdapter;

    /**
     * this shared preferences
     * @see #SHAREDPREFKEY_SETTINGS
     */
    private SharedPreferences       mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();

        return mListView;
    }

    /**
     * initialize this fragment
     * @see #init_sharedPref()
     * @see #init_listitems()
     * @see #init_adapter()
     */
    private void init(){
        init_sharedPref();
        init_listitems();
        init_adapter();
    }
    /**
     * initialize this shared preferences with listener
     */
    private void init_sharedPref(){

        mSharedPref = getActivity().getSharedPreferences(SHAREDPREFKEY_SETTINGS, getActivity().MODE_PRIVATE);
        mSharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {

                }
            }
        });
    }

    /**
     * initialize this listitems<br>
     * {@link #mSharedPref} must be called in prior of this
     * @see #init_sharedPref()
     */
    private void init_listitems(){

    }
    /**
     * initialize this adapter
     * {@link #init_listitems()} must be called in prior of this
     * @see #init_listitems()
     */
    private void init_adapter(){
        mListView = new ListView(getActivity());

        // add items to adapter
        List<SettingsListItem> listitems = new ArrayList<>();


        mListAdapter = new SettingsListAdapter(getActivity().getApplicationContext(), R.layout.activity_settings);
        mListAdapter.addAll(listitems);
        mListView.setAdapter(mListAdapter);
    }

}
