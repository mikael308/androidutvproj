package com.example.mikael.androidutvproj;

/**
 * interface used for callback function when using ListView
 * @author Mikael Holmbom
 * @version 1.0
 */
public interface OnListItemClickListener<T>{
    /**
     * on listitem click
     * @param selectedItem selected item
     */
    void onItemClick(T selectedItem, int position);
    /**
     * on listitem long click
     * @param selectedItem selected item
     */
    void onItemLongClick(T selectedItem, int position);
}
