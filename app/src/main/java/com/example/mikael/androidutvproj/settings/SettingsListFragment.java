package com.example.mikael.androidutvproj.settings;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.mikael.androidutvproj.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings used for this app<br>
 * fragment calls calling class on menu options
 * settings values stored in shared preferences
 * @author Mikael Holmbom
 * @version 1.0
 * @see SettingsListAdapter
 * @see SettingsListItem
 */
public class SettingsListFragment extends ListFragment {

    /**
     * this listadapter
     */
    private SettingsListAdapter     mListAdapter;
    /**
     * listitem : language settigns<br>
     * key: Settings.SHAREDPREFKEY_LANGUAGE
     */
    private SettingsListItem        mListitem_lang;
    /**
     * listitem : photo source<br>
     * key: Settings.SHAREDPREFKEY_PHOTOSRC
     */
    private SettingsListItem        mListitem_photosrc;
    /**
     * this shared preferences<br>
     * key: Settings.SHAREDPREFKEY_SETTINGS
     */
    private SharedPreferences       mSharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();

        // add items to adapter
        List<SettingsListItem> listitems = new ArrayList<>();
        listitems.add(mListitem_lang);
        listitems.add(mListitem_photosrc);

        ListView listView = new ListView(getActivity());
        mListAdapter = new SettingsListAdapter(getActivity(), listView.getId());
        mListAdapter.addAll(listitems);
        setListAdapter(mListAdapter);

        return listView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * initialize this fragment
     * @see #init_sharedPref()
     * @see #init_listitems()
     */
    private void init(){
        init_sharedPref();
        init_listitems();
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
                    case SHAREDPREFKEY_PHOTOSOURCE:
                        mListitem_photosrc.setInfo(sharedPreferences.getString(key, ""));
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

        // PHOTO SOURCE
        ////////////////////////////////////
        String photosrc_header = getResources().getString(R.string.settings_listitem_photosrc_header);
        String photosrc_info = mSharedPref.getString(SHAREDPREFKEY_PHOTOSOURCE, "");
        mListitem_photosrc  = new SettingsListItem(getActivity(), photosrc_header, photosrc_info);
        mListitem_photosrc  .setInfo(photosrc_info);
        mListitem_photosrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_photoSource().show();
            }
        });
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

    /**
     * recursive method<br>
     * get list of file f subdirectories
     * @param paths current list of subdirectory paths
     * @param f rootfile
     * @return list of file f subdirectories
     */
    private List<String> getSubDirectoryPaths(List<String> paths, File f){
        if (f.isDirectory())   paths.add(f.getAbsolutePath());

        File[] flist = f.listFiles();
        if (flist == null)     return paths;

        for (int i = 0; i < flist.length; i++){
            File tf = flist[i];
            if (tf.isDirectory()){
                paths = getSubDirectoryPaths(paths, tf);
            }
        }

        return paths;
    }

    /**
     * settings dialog : set photo source<br>
     * if confirmed: shared preference {@link #SHAREDPREFKEY_PHOTOSOURCE} is set to new value
     * @return settings dialog
     */
    private AlertDialog dialog_photoSource(){
        List<String> listitems = new ArrayList<>();

        File f = Environment.getExternalStorageDirectory().getParentFile();
        LinearLayout layout = new LinearLayout(getActivity());
        layout.addView(new SettingsListItem(getActivity(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()));
        layout.addView(new SettingsListItem(getActivity(), f.getAbsolutePath().toString()));

        listitems = getSubDirectoryPaths(listitems, f);
        for (String dirpath : listitems){
            layout.addView(new SettingsListItem(getActivity(), dirpath));
        }
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(layout);

        final List<String> PHOTOSRC = listitems;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listitems);
        return builder
                .setTitle(getResources().getString(R.string.settings_listitem_photosrc_header))
                .setIcon(R.drawable.ic_camera_roll_black_24dp)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String photosrc = PHOTOSRC.get(which);
                        getActivity().getSharedPreferences(SHAREDPREFKEY_SETTINGS, getActivity().MODE_PRIVATE).edit()
                                .putString(SHAREDPREFKEY_PHOTOSOURCE, photosrc)
                                .apply();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.btn_cancel), null)
                .create();
    }
}
