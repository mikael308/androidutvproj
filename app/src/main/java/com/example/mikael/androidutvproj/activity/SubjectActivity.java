package com.example.mikael.androidutvproj.activity;


import android.support.v7.app.AppCompatActivity;

import com.example.mikael.androidutvproj.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * part of GoF-pattern: Observer<br>
 *     class is thread-safe
 * @author Mikael Holmbom
 * @version 1.0
 * @see Observer
 */
public abstract class SubjectActivity extends AppCompatActivity {

    /**
     * list of attached Observer instances<br>
     * is synchronized with {@link #lock_observerList}
     */
    private List<Observer> mObservers = new ArrayList<>();
    /**
     * lock used when accessing {@link #mObservers}
     */
    private Object lock_observerList = new Object();

    /**
     * update observer instance
     * @param observer
     */
    protected void update(Observer observer){
        synchronized (lock_observerList){
            observer.update();
        }
    }

    /**
     * Update Observer instances attached to this Subject
     */
    protected void updateAllObservers(){
        synchronized(lock_observerList) {
            for (Observer observer : getObservers()) {
                if (observer == null) continue;

                update(observer);
            }
        }
    }

    /**
     * attach Observer to this Subject
     * @param observer
     */
    protected void attach(Observer observer){

        synchronized(lock_observerList){
            if(! getObservers().contains(observer)){
                getObservers().add(observer);
            }
        }
    }

    /**
     * attach list of Observer to this Subject
     * @param observers
     */
    protected void attach(Observer... observers){
        for (Observer observer : observers)
            attach(observer);
    }

    /**
     * detach list of Observer from this Subject
     * @param observers
     */
    protected void detach(Observer... observers){
        for (Observer observer : observers)
            detach(observer);
    };

    /**
     * detach observer from this subject
     * @param observer
     * @return
     */
    protected boolean detach(Observer observer){

        synchronized (lock_observerList){
            return getObservers().remove(observer);
        }
    }

    /**
     * get list of current attached Observer instances
     * @return
     */
    protected List<Observer> getObservers(){
        
        synchronized (lock_observerList){
            return mObservers;
        }
    }
}
