package com.example.mikael.androidutvproj.tool;

import android.app.Activity;

/**
 * runs a method in workerthread then starts UIthread after worker is finished
 * @author Mikael Holmbom
 * @version 1.0
 */
public abstract class PostponedUITask2 extends Thread {


    private Activity mActivity;

    public PostponedUITask2(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        super.run();

        onWorkerThread();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onUIThread();
            }
        });

    }

    /**
     * method running in workerthread
     */
    public abstract void onWorkerThread();

    /**
     * method running in main UIthread
     */
    public abstract void onUIThread();
}
