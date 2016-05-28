package com.example.mikael.androidutvproj.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.DataAccessObject;

import java.util.concurrent.CountDownLatch;

/**
 * extended AsyncTask that shows a progressdialog during work<br>
 *     work with database should be done in method {@link #databaseWork(Object[])}<br>
 *     on post execution, abstract {@link #onSuccess()} is called on UI thread<br>
 *     to set the progress dialog message, use {@link #publishProgress(Object[])} with message string argument in {@link #databaseWork(Object[])}<br>
 * local CountDownLatch countdowns on postexecute, when database work is finished. To access attr use {@link #getCountDownLatch()}
 *
 *
 * @author  Mikael Holmbom
 * @version 1.0
 * @since 2016-04-11
 * @param <T> the object to use as execute parameter, see {@link #execute(Object[])}, {@link #databaseWork(Object[])}
 */
public abstract class DatabaseTask<T> extends AsyncTask<T, String, Boolean>{


    private ProgressDialog mDialog;

    private Context mContext;

    private String mProgressMessage;
    /**
     * current Entry used<br>
     * on access, use synchronized on {@link #lock_rollbackItem}
     */
    private DataAccessObject mRollbackItem = null;
    /**
     * lock used on accessing {@link #mRollbackItem}
     */
    private Object lock_rollbackItem = new Object();


    private final CountDownLatch mCountDownLatch = new CountDownLatch(1);


    public DatabaseTask(Context context, int progressString){
        mContext = context;
        mProgressMessage = mContext.getResources().getString(progressString);
    }
    public DatabaseTask(Context context, String progressString){
        mContext = context;
        mProgressMessage = progressString;
    }

    private void initProgressDialog(){

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(R.string.database_loading_msg));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(true);
        mDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, mContext.getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(true);
            }
        });

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        initProgressDialog();
        mDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        for(String value : values)
            mDialog.setMessage(mProgressMessage + " " + value);
    }

    @Override
    protected Boolean doInBackground(T... items) {
        boolean databaseWorkSuccess = databaseWork(items);
        if (! databaseWorkSuccess && ! isCancelled()){
            onRollback();
        }

        if (isCancelled())
            return false;

        return databaseWorkSuccess;
    }


    @Override
    protected void onPostExecute(Boolean databaseWorkSuccess) {
        super.onPostExecute(databaseWorkSuccess);

        if(databaseWorkSuccess){ // databaseWork finished successfully
            onSuccess();

        } else {
            onRollback();
        }

        mDialog.dismiss();
        mCountDownLatch.countDown();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        onRollback();
    }

    /**
     * main runPostponedUITask with database<br>
     *     will run {@link #onSuccess()} or {@link #onRollback()} depending on the return result from this
     *     method<br>
     * If this method return true: {@link #onSuccess()} is called. If this method return false:
     * {@link #onRollback()} is called
     *
     * @param items
     * @return if databsework was successful return true,
     * if unsuccessful and need to onRollback return false
     */
    public abstract boolean databaseWork(T... items);

    /**
     * runs on UI thread after {@link #databaseWork(Object[])} if return value is false<br>
     *     to get rollbackitem from last used item in past task, use {@link #getRollbackItem()}
     */
    public abstract void onRollback();

    /**
     * runs on UI thread after {@link #databaseWork(Object[])} if return value is true
     */
    public abstract void onSuccess();

    /**
     * get this CountDownLatch<br>
     * CountDownLatch will call countdown when this DatabaseTask is finished,
     * regardless of the task result
     * @return
     */
    public CountDownLatch getCountDownLatch(){
        return mCountDownLatch;
    }

    protected DataAccessObject getRollbackItem(){
        synchronized (lock_rollbackItem){
            return mRollbackItem;
        }
    }

    /**
     * clones param rollbackItem and add to this attr
     * @param rollbackItem
     */
    protected void setRollbackItem(DataAccessObject rollbackItem){
        synchronized(lock_rollbackItem){
            mRollbackItem = rollbackItem.clone();
        }
    }



}
