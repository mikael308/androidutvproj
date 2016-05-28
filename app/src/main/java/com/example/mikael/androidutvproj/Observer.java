package com.example.mikael.androidutvproj;

import com.example.mikael.androidutvproj.activity.Subject;
import com.example.mikael.androidutvproj.dao.DataAccessObject;

/**
 * part of GoF-pattern: Observer
 * @author Mikael Holmbom
 * @version 1.0
 * @see Subject
 */
public interface Observer<T extends DataAccessObject> {

    String getObserverId();

    /**
     * update this observer
     */
    void update();

}
