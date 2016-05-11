package com.example.mikael.androidutvproj.settings;

import android.content.Context;
import android.widget.Switch;

/**
 * Settings Listitem using content from {@link SettingsListItem} and add switch button<br>
 *     Used for on/off setting options
 * @author Mikael Holmbom
 * @verison 1.0
 */
public class SettingsListSwitchItem extends SettingsListItem {

    /**
     * this settings value
     */
    private Switch mBtn_switch;

    public SettingsListSwitchItem(Context context) {
        super(context);
    }

    public SettingsListSwitchItem(Context context, String header) {
        super(context, header);
    }

    @Override
    protected void init() {
        super.init();

        mBtn_switch = new Switch(getContext());
        mBtn_switch.setClickable(false);

        addView(mBtn_switch);
    }

    @Override
    public boolean performClick() {
        mBtn_switch.performClick();
        return super.performClick();
    }

    /**
     * determine if this listitem is checked
     * @return true if this switch is true
     */
    public boolean isChecked(){
        return mBtn_switch.isChecked();
    }

    /**
     * set checkedvalue to this listitems switch
     * @param checkedValue new checkedvalue
     */
    public void setChecked(boolean checkedValue){
        mBtn_switch.setChecked(checkedValue);
    }
}
