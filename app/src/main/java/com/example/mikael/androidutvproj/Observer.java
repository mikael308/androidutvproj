package com.example.mikael.androidutvproj;

import com.example.mikael.androidutvproj.activity.SubjectActivity;

/**
 * part of GoF-pattern: Observer
 * @author Mikael Holmbom
 * @version 1.0
 * @see SubjectActivity
 */
public interface Observer {

    String getObserverId();

    /**
     * update this observer
     */
    void update();

}
