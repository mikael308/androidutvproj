package com.example.mikael.androidutvproj.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.mikael.androidutvproj.dao.ChildDataAccessObject;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.dao.ParentDataAccessObject;
import com.example.mikael.androidutvproj.dao.Photo;
import com.example.mikael.androidutvproj.dao.RealEstate;
import com.example.mikael.androidutvproj.dao.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Facade for handling operations with database
 *
 * @author Mikael Holmbom
 * @version 1.0
 * @since 2016-04-11
 *
 * @see RealEstateHelper
 * @see PhotoHelper
 * @see EventHelper
 */
 class DatabaseFacade {

    /**
     * this Context
     */
    private static Context mContext;

    ///////////////////
    // TABLE HELPERS
    /////////////////////
    /**
     * helper handling Event<br>
     *     @see Event
     */
    private static EventHelper mEventHelper;
    /**
     * helper handling Photo<br>
     *     @see Photo
     */
    private static PhotoHelper mPhotoHelper;
    /**
     * helper handling RealEstate<br>
     *     @see RealEstate
     */
    private static RealEstateHelper mRealEstateHelper;


    public DatabaseFacade(Context context){
        mContext = context.getApplicationContext();
    }

    public static void setContext(Context context){
        mContext = context;
    }

    private static boolean initHelpers(){
        threadlog("initHelpers()");
        mRealEstateHelper   = new RealEstateHelper(mContext);
        mEventHelper        = new EventHelper(mContext);
        mPhotoHelper        = new PhotoHelper(mContext);

        try {
            mRealEstateHelper.init();
            mEventHelper.init();
            mPhotoHelper.init();

        } catch(SQLException e){
            Log.e("databasefacade", e.getMessage());
            if (mRealEstateHelper != null)      mRealEstateHelper   .dropTable();
            if (mEventHelper != null)           mEventHelper        .dropTable();
            if (mPhotoHelper != null)           mPhotoHelper        .dropTable();

            return false;
        }

        return true;
    }

    public static Context getContext(){
        return mContext;
    }

    private static void threadlog(String msg){
        Log.d("databasefacade", msg + " in : " + Thread.currentThread().getName());
    }


    /**
     * initialize Database
     * @return true if initialize was successful
     */
    public static boolean init(){

        return initHelpers();

    }

    /**
     * get specific entry row from database
     * @param entry entry to get
     * @return DataAccessObject representing row from database
     */
    public static DataAccessObject getEntry(DataAccessObject entry){
        if (entry instanceof RealEstate)
            return mRealEstateHelper.parse(mRealEstateHelper.getRow((RealEstate) entry));

        if (entry instanceof Photo)
            return mPhotoHelper.parse(mPhotoHelper.getRow((Photo) entry));

        if (entry instanceof Event)
            return mEventHelper.parse(mEventHelper.getRow((Event) entry));

        return null;
    }

    /**
     * persist entry to database
     * @param entry entry to persist
     * @return true if entry was successfully persisted
     */
    public static boolean persist(DataAccessObject entry){
        long res = -1;

        if (entry instanceof RealEstate){
            res = mRealEstateHelper.persist((RealEstate) entry);

        } else if (entry instanceof Photo){
            res = mPhotoHelper.persist((Photo) entry);

        } else if (entry instanceof Event){
            res = mEventHelper.persist((Event) entry);
        }

        return res >= 0;
    }

    /**
     * replace entry in database with same id as param entry
     * @param entry entry to replace
     * @return true if entry was successfully replaced
     */
    public static boolean replace(DataAccessObject entry){
        long res = -1;

        if (entry instanceof RealEstate){
            res = mRealEstateHelper.replace((RealEstate) entry);

        } else if (entry instanceof Photo){
            res = mPhotoHelper.replace((Photo) entry);

        } else if (entry instanceof Event){
            res = mEventHelper.replace((Event) entry);
        }

        return res >= 0;
    }

    /**
     * delete entry from database
     * @param entry entry to delete
     * @return true if entry was successfully deleted
     */
    public static boolean delete(DataAccessObject entry){
        long res = -1;

        if (entry instanceof ParentDataAccessObject){
            res = deleteGreedy((ParentDataAccessObject) entry);

        } else if (entry instanceof ChildDataAccessObject){
            res = deleteLazy((ChildDataAccessObject) entry);
        }

        return res >= 0;
    }

    /**
     * delete RealEstate from database along with its associated child entries
     * @param entry entry to delete
     * @return affected databaserow, -1 if nothing changed
     */
    public static long deleteGreedy(final ParentDataAccessObject entry){

        long res = -1;
        if(entry instanceof RealEstate){
            RealEstate re = (RealEstate) entry;
            res = mRealEstateHelper.delete(re);

            // DELETE ENTRIES CHILDS

            for(Event showing : re.getShowings())
                mEventHelper.delete(showing);

            for(Photo photo : re.getPhotos())
                mPhotoHelper.delete(photo);

        }

        return res;
    }

    /**
     * delete entry only
     * @param entry entry to delete
     * @return affected databaserow, -1 if nothing changed
     */
    private static long deleteLazy(ChildDataAccessObject entry){

        if (entry instanceof Photo)
            return mPhotoHelper.delete((Photo) entry);

        if (entry instanceof Event)
            return mEventHelper.delete((Event) entry);


        return -1;
    }


    /**
     * get list of combined database entries
     * @return
     */
    public static List<RealEstate> readEntries(){
        threadlog("readEntries()");
        return combineWithChildren(mRealEstateHelper.getAllEntries());

    }


    /**
     * Reads the child entries of entry elements in param realEstates
     * @param realEstates list of RealEstate items without data from child entries
     * @return list of RealEstate containing data from related child entries
     */
    private static List<RealEstate> combineWithChildren(List<RealEstate> realEstates){
        if(realEstates == null || realEstates.isEmpty())       return realEstates;
        Cursor cEvents = mEventHelper.getAllRows();
        Cursor cPhotos = mPhotoHelper.getAllRows();

        Map<String, ArrayList<Event>> events = mEventHelper.getRowsGroupByColumn(EventHelper.COLUMN_FOREIGN_ID);
        Map<String, ArrayList<Photo>> photos = mPhotoHelper.getRowsGroupByColumn(PhotoHelper.COLUMN_FOREIGN_ID);

        for (RealEstate realEstate : realEstates){

            // EVENTS
            if(events != null) {
                List<Event> eventList;
                if ((eventList = events.get(realEstate.getId())) != null) {
                    realEstate.getShowings().addAll(eventList);
                }
            }

            // PHOTOS
            if(photos != null) {
                List<Photo> photoList;
                if ((photoList = photos.get(realEstate.getId())) != null) {
                    realEstate.getPhotos().addAll(photoList);

                }
            }
        }

        return realEstates;
    }


}
