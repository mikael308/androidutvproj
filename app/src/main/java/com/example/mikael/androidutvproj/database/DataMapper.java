package com.example.mikael.androidutvproj.database;

import android.app.Activity;
import android.util.Log;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.activity.Subject;
import com.example.mikael.androidutvproj.dao.ChildDataAccessObject;
import com.example.mikael.androidutvproj.dao.DataAccessObject;
import com.example.mikael.androidutvproj.dao.Event;
import com.example.mikael.androidutvproj.dao.Photo;
import com.example.mikael.androidutvproj.dao.RealEstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Mapper between database and application<br>
 *     holds a buffer of database entries as dao-instances
 * @author Mikael Holmbom
 * @version 1.0
 */
public class DataMapper extends Subject {

    public final static int CURRENT_STATE_OK                    = 0;
    public final static int CURRENT_STATE_LISTEMPTY             = 1;
    public final static int CURRENT_STATE_NOTSELECTED           = 2;

    /**
     * index of current used RealEstate<br>
     *     instance is stored in DatabaseFacade.
     *     to get instance, use {@link #getCurrentRealEstate()}
     */
    private static int mCurrentItemIdx_buffer = -1;

    private static List<RealEstate> mItemList_buffer = null;



    public final static Comparator<RealEstate> REALESTATE_SORTORDER_NATURAL = new Comparator<RealEstate>() {
        @Override
        public int compare(RealEstate lhs, RealEstate rhs) {
            return lhs.compareTo(rhs);
        }
    };
    public final static Comparator<RealEstate> REALESTATE_SORTORDER_RENT_ASC = new Comparator<RealEstate>() {
        @Override
        public int compare(RealEstate lhs, RealEstate rhs) {
            if (lhs.getRent() == rhs.getRent())
                return 0;
            if (lhs.getRent() < rhs.getRent())
                return -1;
            else
                return 1;

        }
    };
    public final static Comparator<RealEstate> REALESTATE_SORTORDER_SHOWINGS_ASC = new Comparator<RealEstate>() {
        @Override
        public int compare(RealEstate lhs, RealEstate rhs) {
            if (lhs.getShowings() == null)
                return 1;
            if (rhs.getShowings() == null)
                return -1;

            long lhs_min = Long.MAX_VALUE;
            long rhs_min = Long.MAX_VALUE;

            for (Event s : lhs.getShowings()) {
                if (!s.isPast()) {
                    long stime = s.getDate().getTime();
                    if (stime < lhs_min)
                        lhs_min = stime;
                }
            }
            for (Event s : rhs.getShowings()) {
                if (!s.isPast()) {
                    long stime = s.getDate().getTime();
                    if (stime < rhs_min)
                        rhs_min = stime;
                }
            }
            if (lhs_min == rhs_min)
                return 0;
            if (lhs_min < rhs_min)
                return -1;
            else
                return 1;
        }
    };

    private static Comparator<Photo> PHOTO_SORTORDER_DATE_ASC = new Comparator<Photo>() {
        @Override
        public int compare(Photo lhs, Photo rhs) {
            if(lhs.getDate() == null)
                return 1;
            if(lhs.getDate() == null)
                return -1;

            long lhstime = lhs.getDate().getTime();
            long rhstime = rhs.getDate().getTime();

            if(lhstime == rhstime)
                return 0;
            if(lhstime < rhstime)
                return -1;

            return 1;
        }
    };


    private static Comparator<RealEstate> mCurrentRealEstateSortorder = REALESTATE_SORTORDER_NATURAL;

    private static Comparator<Photo> mCurrentPhotosSortorder = PHOTO_SORTORDER_DATE_ASC;

    //TODO tmp dbg tool
    private static int DBG_DBWORK_DELAY = 0;



    public static void setCurrentRealEstate(RealEstate realEstate){
        if(realEstate == null)
            mCurrentItemIdx_buffer = -1;
        else
            mCurrentItemIdx_buffer = getRealEstateList().indexOf(realEstate);

    }

    public static RealEstate getCurrentRealEstate(){
        if (mCurrentItemIdx_buffer < 0) return null;

        return getRealEstateList().get(mCurrentItemIdx_buffer);
    }

    public static void incrIdx(){
        mCurrentItemIdx_buffer = ++mCurrentItemIdx_buffer % getRealEstateList().size();
    }

    public static void decrIdx(){
        mCurrentItemIdx_buffer--;
        while (mCurrentItemIdx_buffer < 0)
            mCurrentItemIdx_buffer += getRealEstateList().size();
    }

    /**
     * get list of RealEstate
     * @return
     */
    public static List<RealEstate> getRealEstateList(){
        return mItemList_buffer;

    }

    /**
     * return state of this Current RealEstate<br>
     * <ul>
     *     <li>0: current state is OK</li>
     *     <li>1: list of realestate is empty or null</li>
     *     <li>2: current realestate is not selected</li>
     * </ul>
     * @return
     */
    public static int getCurrentState(){

        if (getCurrentRealEstate() == null) {
            if (DataMapper.getRealEstateList() == null || DataMapper.getRealEstateList().isEmpty()){
                return CURRENT_STATE_LISTEMPTY;

            } else {
                return CURRENT_STATE_NOTSELECTED;
            }
        }

        return CURRENT_STATE_OK;
    }


    /**
     * get the parent of param entry
     * @param entry
     * @return
     */
    public static RealEstate getParent(ChildDataAccessObject entry){
        for (RealEstate parent : getRealEstateList()){
            if(parent.getId().equals(entry.getForeignKey()))
                return parent;
        }
        return null;
    }

    public static CountDownLatch init(final Activity activity){
        DatabaseFacade.setContext(activity);

        DatabaseTask<Void> task = new DatabaseTask<Void>(activity, activity.getString(R.string.database_task_label_init)) {

            @Override
            public boolean databaseWork(Void... items) {
                publishProgress(activity.getString(R.string.database_publish_init));

                if(! DatabaseFacade.init())
                    return false;

                mItemList_buffer = DatabaseFacade.readEntries();
                if (mItemList_buffer != null
                        && ! mItemList_buffer.isEmpty()
                        && getCurrentRealEstate() == null){
                    setCurrentRealEstate(mItemList_buffer.get(0));
                } else if (mItemList_buffer == null) {
                    mItemList_buffer = new ArrayList<>();
                }
                sort();

                return true;
            }

            @Override
            public void onRollback() {

                Log.d("DataMapper", "error on initialize database");
            }

            @Override
            public void onFeedbackPost(boolean databaseWorkSuccess) {

            }
        };
        task.execute();

        return task.getCountDownLatch();
    }

    /**
     * read entries from database to field list<br>
     *     all databasework is done in separate AsyncTask thread
     * @param activity
     * @return
     */
    public static CountDownLatch readEntries(final Activity activity){

        DatabaseTask<Void> task = new DatabaseTask<Void>(activity, activity.getString(R.string.database_task_label_init)) { //TODO fixa string res

            @Override
            public boolean databaseWork(Void... items) {
                publishProgress(activity.getString(R.string.database_publish_init));

                mItemList_buffer = DatabaseFacade.readEntries();

                //simDatabaseWork(DBG_DBWORK_DELAY); //TODO tmp dbg

                return true;
            }

            @Override
            public void onRollback() {
                mItemList_buffer = null;
            }

            @Override
            public void onFeedbackPost(boolean databaseWorkSuccess) {

            }
        };
        task.execute();
        return task.getCountDownLatch();
    }


    //TODO tmp debug tool
    private static void simDatabaseWork(int sleeptime){
        try{
            Thread.sleep(sleeptime);

        } catch(InterruptedException e){        }
    }

    public static  <Entry extends DataAccessObject> CountDownLatch add(final Activity activity, final Entry entry){

        DatabaseTask<Entry> task = new DatabaseTask<Entry>(activity, R.string.database_task_label_add){
            @Override
            public boolean databaseWork(Entry... items) {
                for(Entry entry : items){
                    publishProgress(entry.getLabel());
                    setRollbackItem(entry);

                    if (! add(entry))
                        return false;

                    if (entry instanceof RealEstate){
                        sort();
                        setCurrentRealEstate((RealEstate) entry);
                    }

                    //simDatabaseWork(DBG_DBWORK_DELAY); //TODO tmp debug
                }
                return true;
            }

            @Override
            public void onRollback() {
                delete(getRollbackItem());

            }

            @Override
            public void onFeedbackPost(boolean databaseWorkSuccess) {

                if (databaseWorkSuccess){
                    updateAllObservers();
                    if (entry instanceof RealEstate){
                        updateAddAllObservers((RealEstate) entry);
                    }
                }
            }
        };

        task.execute(entry);
        return task.getCountDownLatch();
    }

    public static <Entry extends DataAccessObject> CountDownLatch delete(final Activity activity, final Entry entry){

        DatabaseTask<Entry> task = new DatabaseTask<Entry>(activity, R.string.database_task_label_delete) {
            @Override
            public boolean databaseWork(Entry... items) {
                for (Entry entry : items){
                    publishProgress(entry.getLabel());
                    setRollbackItem(entry);

                    if (! delete(entry)){
                        return false;
                    }

                    //simDatabaseWork(DBG_DBWORK_DELAY); //TODO tmp debug
                }
                return true;
            }

            @Override
            public void onRollback() {

                try {
                    ChildDataAccessObject entry = (ChildDataAccessObject) getRollbackItem();
                    add(entry);

                } catch(ClassCastException e){
                    add(getRollbackItem());
                }
                sort();
            }

            @Override
            public void onFeedbackPost(boolean databaseWorkSuccess) {

                if(databaseWorkSuccess){
                    if (entry instanceof RealEstate){
                        updateDeleteAllObservers((RealEstate) entry);
                    }

                    updateAllObservers();
                }
            }
        };
        task.execute(entry);

        return task.getCountDownLatch();
    }

    public static <Entry extends DataAccessObject> CountDownLatch edit(Activity activity, final Entry entry){

        DatabaseTask<Entry> task = new DatabaseTask<Entry>(activity, R.string.database_task_label_edit) {

            @Override
            public boolean databaseWork(Entry... items) {
                for (Entry entry : items) {
                    publishProgress(entry.getLabel());
                    setRollbackItem(DatabaseFacade.getEntry(entry));

                    if (! edit(entry))
                        return false;

                    //simDatabaseWork(DBG_DBWORK_DELAY); //TODO tmp debug
                }

                return true;
            }

            @Override
            public void onRollback() {
                edit(getRollbackItem());
            }

            @Override
            public void onFeedbackPost(boolean databaseWorkSuccess) {

                if(databaseWorkSuccess){
                    updateAllObservers();
                }
            }
        };
        task.execute(entry);

        return task.getCountDownLatch();
    }

    public static void setSortOrder(Comparator<RealEstate> comp){
        mCurrentRealEstateSortorder = comp;
    }

    /**
     * sort current list of items according to sortorder defined as {@link #mCurrentRealEstateSortorder} with {@link #setSortOrder(Comparator)}
     */
    public static void sort(){
        Collections.sort(mItemList_buffer, mCurrentRealEstateSortorder);
    }

    public static void sortPhotos(RealEstate re){
        Collections.sort(re.getPhotos(), mCurrentPhotosSortorder);
    }


    ////////////////////////////////
    //
    // HANDLE CURRENT READ ENTRYLIST
    //
    /////////////////////////////////

    /**
     * adds entry to current buffer<br>
     *     is called after the actual database is edited
     * @param entry
     * @return
     */
    protected static boolean addEntry(DataAccessObject entry){
        if (entry instanceof ChildDataAccessObject){
            return addEntry((ChildDataAccessObject) entry);
        }
        if (entry instanceof RealEstate){
            return getRealEstateList().add((RealEstate) entry);
        }

        return false;
    }

    /**
     * adds entry to current buffer<br>
     *     is called after the actual database is edited
     * @param entry
     * @return
     */
    protected static boolean addEntry(ChildDataAccessObject entry){

        if (entry instanceof Event){
            RealEstate parent = getParent(entry);
            if(parent != null)
                return parent.getShowings().add((Event) entry);

        } else if (entry instanceof Photo){
            RealEstate parent = getParent(entry);
            if(parent != null){

                if(parent.getPhotos().add((Photo) entry)){
                    sortPhotos(parent);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * deletes entry from current buffer<br>
     *     is called after the actual database is edited
     * @param entry
     * @return
     */
    protected static boolean deleteEntry(DataAccessObject entry){
        if (entry instanceof ChildDataAccessObject){
            return deleteEntry((ChildDataAccessObject) entry);
        }

        if (entry instanceof RealEstate){
            RealEstate realEstate   = (RealEstate) entry;
            RealEstate current      = getCurrentRealEstate();

            if (current != null && current.equals(realEstate)){
                List<RealEstate> itemlist = getRealEstateList();

                if (itemlist.size() == 1) { // delete item was last item in list, now list will be empty
                    setCurrentRealEstate(null);

                } else {
                    int curIdx = getRealEstateList().indexOf(realEstate);
                    if (curIdx == itemlist.size()-1){ // delete item was last index in list
                        decrIdx();

                    } else { // delete item was not last index in list

                    }
                }
            }

            return getRealEstateList().remove(entry);
        }

        return false;
    }

    /**
     * deletes entry from current buffer<br>
     *     is called after the actual database is edited
     * @param entry
     * @return
     */
    protected static boolean deleteEntry(ChildDataAccessObject entry){

        if (entry instanceof Event){
            RealEstate parent = getParent(entry);
            if (parent != null)
                return parent.getShowings().remove(entry);

        } else if (entry instanceof Photo){
            RealEstate parent = getParent(entry);
            if (parent != null){
                return parent.getPhotos().remove(entry);
            }

        }
        return false;
    }

    /**
     * edits entry from current buffer<br>
     *     is called after the actual database is edited
     * @param entry
     * @return
     */
    protected static boolean editEntry(DataAccessObject entry) {

        if (entry instanceof RealEstate) {
            getRealEstateList().remove(entry);
            return getRealEstateList().add((RealEstate) entry);

        } else if (entry instanceof Event) {
            for (RealEstate re : getRealEstateList()) {
                if (re.getShowings().remove(entry)) {
                    return re.getShowings().add((Event) entry);
                }
            }

        } else if (entry instanceof Photo) {
            for (RealEstate re : getRealEstateList()) {
                if (re.getPhotos().remove(entry)) {
                    return re.getPhotos().add((Photo) entry);
                }
            }
        }
        return false;
    }

    /**
     * adds entry to current buffer and database
     * @param entry
     * @return
     */
    protected static boolean add(DataAccessObject entry){
        if (entry instanceof ChildDataAccessObject){
            return add((ChildDataAccessObject) entry);
        }

        if (entry instanceof RealEstate){
            RealEstate re = (RealEstate) entry;
            if (DatabaseFacade.persist(re)){
                return addEntry(re);
            }
        }

        return false;
    }

    /**
     * adds entry to current buffer and database
     * @param entry
     * @return
     */
    protected static boolean add(ChildDataAccessObject entry){
        if (DatabaseFacade.persist(entry)){
            return addEntry(entry);
        }

        return false;
    }

    /**
     * deletes entry from current buffer and database
     * @param entry
     * @return
     */
    protected static boolean delete(DataAccessObject entry){
        if (DatabaseFacade.delete(entry)){
            return deleteEntry(entry);
        }

        return false;
    }
    /**
     * edits entry from current buffer and database
     * @param entry
     * @return
     */
    protected static boolean edit(DataAccessObject entry){
        if (DatabaseFacade.replace(entry)){
            return editEntry(entry);
        }

        return false;
    }


}
