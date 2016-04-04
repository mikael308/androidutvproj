package com.example.mikael.androidutvproj.settings;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

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
     * shared preferences key : this app language
     */
    public static final String SHAREDPREFKEY_LANGUAGE = "SHAREDPREFKEY_LANGUAGE";

    /**
     * this listview
     */
    private ListView                mListView;
    /**
     * this listadapter
     */
    private SettingsListAdapter     mListAdapter;

    /**
     * listitem : language settigns
     * @see #SHAREDPREFKEY_LANGUAGE
     */
    private SettingsListItem        mListitem_lang;

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
                    case SHAREDPREFKEY_LANGUAGE:
                        mListitem_lang.setInfo(sharedPreferences.getString(key, Lang.ENGLISH.getName()));
                        break;

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

        // LANGUAGE
        ////////////////////////////////////
        String lang_header  = getResources().getString(R.string.settings_listitem_language_header);
        String lang_info    = mSharedPref.getString(SHAREDPREFKEY_LANGUAGE, Lang.ENGLISH.getName());
        mListitem_lang      = new SettingsListItem(getActivity(), lang_header, lang_info);
        mListitem_lang      .setInfo(lang_info);
        mListitem_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_language().show();
            }
        });

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
        listitems.add(mListitem_lang);
        listitems.add(mListitem_photosrc);

        mListAdapter = new SettingsListAdapter(getActivity().getApplicationContext(), R.layout.activity_settings);
        mListAdapter.addAll(listitems);
        mListView.setAdapter(mListAdapter);
    }

    /**
     * settings dialog set app language
     * if confirmed: shared preference {@link #SHAREDPREFKEY_LANGUAGE} is set to new value
     * @return language dialog
     * @see Lang
     */
    private AlertDialog dialog_language(){
        ArrayList<String> listitems = new ArrayList<>();

        LinearLayout layout = new LinearLayout(getActivity());
        for (Lang l : Lang.values()){
            listitems.add(l.getName());
            layout.addView(new SettingsListItem(getActivity(), l.getName()));
        }
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(layout);

        AlertDialog.Builder builder     = new AlertDialog.Builder(getActivity());
        ArrayAdapter<String> adapter    = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listitems);
        return builder
                .setTitle(getResources().getString(R.string.settings_listitem_language_header))
                .setIcon(R.drawable.ic_language_black_24dp)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lang = Lang.findByIdx(which).getName();
                        getActivity().getSharedPreferences(SHAREDPREFKEY_SETTINGS, getActivity().MODE_PRIVATE).edit()
                                .putString(SHAREDPREFKEY_LANGUAGE, lang)
                                .apply();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel), null)
                .create();
    }

}
