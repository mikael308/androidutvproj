package com.example.mikael.androidutvproj.activity;



import com.example.mikael.androidutvproj.Observer;
import com.example.mikael.androidutvproj.dao.RealEstate;

import java.util.ArrayList;
import java.util.List;

/**
 * part of GoF-pattern: Observer<br>
 *     class is thread-safe
 * @author Mikael Holmbom
 * @version 1.0
 * @see Observer
 */
public abstract class Subject {

    /**
     * list of attached Observer instances<br>
     * is synchronized with {@link #lock_observerList}
     */
    private static List<Observer> mObservers = new ArrayList<>();
    /**
     * lock used when accessing {@link #mObservers}
     */
    private static Object lock_observerList = new Object();

    /**
     * updateResetView observer instance
     * @param observer
     */
    protected static void update(Observer observer){
        synchronized (lock_observerList){
            observer.updateResetView();
        }
    }

    /**
     * Update Observer instances attached to this Subject
     */
    protected static void updateAllObservers(){
        synchronized(lock_observerList) {
            for (Observer observer : getObservers()) {
                if (observer == null) continue;

                update(observer);
            }
        }
    }

    protected static void updateAddAllObservers(RealEstate realEstate){
        synchronized(lock_observerList){
            for(Observer observer : getObservers()){
                if(observer == null) continue;

                observer.updateAdd(realEstate);
            }
        }
    }

    protected static void updateDeleteAllObservers(RealEstate realEstate){
        synchronized(lock_observerList){
            for(Observer observer : getObservers()){
                if(observer == null) continue;

                observer.updateDelete(realEstate);
            }
        }
    }

    /**
     * determine if observer is attached
     * @param observer
     * @return
     */
    protected static boolean isAttached(Observer observer){
        synchronized (lock_observerList){
            for(Observer o : getObservers()){
                if(o!= null
                        && o.getObserverId().equals(observer.getObserverId()))
                    return true;
            }
        }
        return false;
    }

    /**
     * remove current observers and set to param observers
     * @param observers new list of Observers
     */
    protected static void setObservers(Observer... observers){
        synchronized (lock_observerList){
            ArrayList<Observer> obs = new ArrayList<>();
            for(Observer o : observers){
                obs.add(o);
            }

            mObservers = obs;
        }
    }

    /**
     * attach Observer to this Subject
     * @param observer
     */
    protected static void attach(Observer observer){
        if(observer == null) return;

        synchronized(lock_observerList){
            if (isAttached(observer))    return; // already added

            getObservers().add(observer);
        }
    }

    /**
     * attach list of Observer to this Subject
     * @param observers
     */
    protected static void attach(Observer... observers){
        for (Observer observer : observers)
            attach(observer);

    }

    /**
     * detach list of Observer from this Subject
     * @param observers
     */
    protected static void detach(Observer... observers){
        for (Observer observer : observers)
            detach(observer);
    };

    /**
     * detach observer from this subject
     * @param observer
     * @return
     */
    protected static boolean detach(Observer observer){

        synchronized (lock_observerList){
            return getObservers().remove(observer);
        }
    }

    /**
     * get list of current attached Observer instances
     * @return
     */
    protected static List<Observer> getObservers(){
        
        synchronized (lock_observerList){
            return mObservers;
        }
    }
}
