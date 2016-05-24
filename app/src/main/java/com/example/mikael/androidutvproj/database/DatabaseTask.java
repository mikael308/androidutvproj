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
 *     on post execution, abstract {@link #post()} is called on UI thread<br>
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

    private DataAccessObject mRollbackItem = null;


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

        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rollback();
                mDialog.dismiss();
            }
        };

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(R.string.database_loading_msg));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(true);
        mDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, mContext.getString(R.string.btn_cancel), cancel);

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
        return databaseWork(items);
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(aBoolean){ // databaseWork finished successfully
            post();

        } else {
            rollback();
        }

        mDialog.dismiss();
        mCountDownLatch.countDown();

        post();
        mDialog.dismiss();
        mCountDownLatch.countDown();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        rollback();
    }

    public abstract void rollback();

    /**
     * main runPostponedUITask with database
     * @param items
     */
    public abstract boolean databaseWork(T... items);

    /**
     * runs on UI thread after {@link #databaseWork(Object[])}
     */
    public abstract void post();

    public CountDownLatch getCountDownLatch(){
        return mCountDownLatch;
    }

    protected DataAccessObject getRollbackItem(){
        return mRollbackItem;
    }

    /**
     * clones param rollbackItem and add to this attr
     * @param rollbackItem
     */
    protected void setRollbackItem(DataAccessObject rollbackItem){
        mRollbackItem = rollbackItem.clone();
    }


}
