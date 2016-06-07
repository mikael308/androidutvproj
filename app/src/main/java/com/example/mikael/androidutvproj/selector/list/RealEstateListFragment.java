package com.example.mikael.androidutvproj.selector.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.database.DataMapper;
import com.example.mikael.androidutvproj.database.DatabaseDialog;


/**
 * fragment listing current Apartments<br>
 * contains :
 * <ul>
 *     <li>list of current Apartments</li>
 * </ul>

 * activity using this fragment must implement interface RealEstateListListener
 *
 * @author Mikael Holmbom
 * @version 1.1
 * @see OnListItemClickListener
 * @see RealEstateListListener
 * @see RealEstate
 */
public class RealEstateListFragment extends ClickableListFragment<RealEstateListItem> {

    public interface RealEstateListListener extends OnListItemClickListener<RealEstate> {
        /**
         * called on action to create new event
         */
        void onNewRealEstate();

        @Override
        void onItemClick(RealEstate selectedItem, int position);

        @Override
        void onItemLongClick(RealEstate selectedItem, int position);
    }



    /**
     * activity using this fragment
     */
    protected RealEstateListListener mRealEstateListListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            if (context instanceof Activity)
                mRealEstateListListener = (RealEstateListListener) context;

        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + RealEstateListListener.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ListView listView = new ListView(getActivity());

        return listView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RealEstateListAdapter listadapter = new RealEstateListAdapter(getActivity());
        setListAdapter(listadapter);

        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();

        RealEstateListAdapter listAdapter = (RealEstateListAdapter) getListAdapter();
        listAdapter.updateSettings();
    }

    private void initListeners(){
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRealEstateListListener.onItemClick((RealEstate) getListAdapter().getItem(position), position);
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mRealEstateListListener.onItemLongClick((RealEstate) getListAdapter().getItem(position), position);
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_realestate_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_new:
                DatabaseDialog.newRealEstate(getActivity())
                        .show(getActivity().getSupportFragmentManager(), "newRealEstate");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public String getObserverId() {
        return getClass().toString();
    }

    @Override
    public void updateData() {
        //TODO ta bort alla dessa updateData...

    }

    @Override
    public void updateResetView() {
        Log.d("listfragment", "resetView");

        RealEstateListAdapter listadapter = (RealEstateListAdapter) getListAdapter();

        listadapter.notifyDataSetChanged();

    }

    @Override
    public void updateAdd(DataAccessObject item) {

        getListView()
                .smoothScrollToPosition(
                        DataMapper.getRealEstateList().indexOf(item));

    }

    @Override
    public void updateDelete(DataAccessObject item) {

    }
}
