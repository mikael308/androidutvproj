package com.example.mikael.androidutvproj.display;

/**
 * Listener class with callback methods to override when used for Dialog button clicklisteners<br>
 *     as standard all click methods return true
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class DialogButtonListener {

    /**
     * is called on Dialog positive button click<br>
     * return true if dialog status is ok and should be closed<br>
     *     return false if dialog should stay visible
     * @return true if dialog status is ok
     */
    public boolean onPositiveClick(){
        return true;
    }
    /**
     * is called on Dialog negative button click<br>
     * return true if dialog status is ok and should be closed<br>
     *     return false if dialog should stay visible
     * @return true if dialog status is ok
     */
    public boolean onNegativeClick(){
        return true;
    }
    /**
     * is called on Dialog neutral button click<br>
     * return true if dialog status is ok and should be closed<br>
     *     return false if dialog should stay visible
     * @return true if dialog status is ok
     */
    public boolean onNeutralClick(){
        return true;
    }

}
