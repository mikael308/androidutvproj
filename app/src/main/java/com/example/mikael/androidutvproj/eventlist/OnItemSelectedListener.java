package com.example.mikael.androidutvproj.eventlist;

/**
 * interface used for callback function when using
 * @author Mikael Holmbom
 * @version 1.0
 */
public interface OnItemSelectedListener<T>{
    /**
     * on listitem click
     * @param selectedItem selected item
     */
    void onItemSelected(T selectedItem);
    /**
     * on listitem long click
     * @param selectedItem selected item
     */
    void onItemLongClick(T selectedItem);
}
