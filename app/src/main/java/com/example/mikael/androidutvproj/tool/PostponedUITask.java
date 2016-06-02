package com.example.mikael.androidutvproj;

import android.app.Activity;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Executes task on UI thread after waiting on CountDownLatch<br>
 * Method {@link #runPostponedUITask()} is executed on UI-thread when ctor param CountDownLatch is 0<br>
 *     to start waiting, use {@link #start()}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class PostponedUITask extends Thread {

    /**
     * latch to await for in prior of {@link #runPostponedUITask()}
     *
     */
    private CountDownLatch mLatch;
    /**
     * calling activity<br>
     *     will use this activities UIthread
     *
     */
    private Activity mActivity;

    /**
     *
     * @param activity calling activity:
     *     will use this activities UIthread
     * @param latch latch to await for in prior of {@link #runPostponedUITask()}
     */
    public PostponedUITask(Activity activity, CountDownLatch latch){
        mActivity   = activity;
        mLatch      = latch;
    }

    @Override
    public final void run() {
        super.run();

        while(true) {
            try {
                mLatch.await();
                break;
            } catch (InterruptedException e) {

            }
        }


        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                runPostponedUITask();
            }
        });
    }

    /**
     * the postponed runPostponedUITask<br>
     *     runs on UI thread in activity: {@link #mActivity}
     */
    public abstract void runPostponedUITask();


}
