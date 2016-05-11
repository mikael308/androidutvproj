package com.example.mikael.androidutvproj.display;

/**
 * callback methods called from {@link ImageNavigatorFragment}
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public interface ImageNavigatorListener  {

    /**
     * called between animations when user navigates left
     */
    void navigateLeft();
    /**
     * called between animations when user navigates right
     */
    void navigateRight();

}
