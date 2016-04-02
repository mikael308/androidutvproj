package com.example.mikael.androidutvproj.listEvents;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * class used as listitem containing a <Code>Event</Code> instance
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.0
 *
 */
public class ListEventItem extends LinearLayout {

    private TextView mName;
    private TextView mDescription;

    public ListEventItem(Context context) {
        super(context);
        mName           = new TextView(getContext());
        mDescription    = new TextView(getContext());
        mDescription.setEms(20);

        init();
    }

    /**
     * initialize this Layout
     */
    private void init(){
        this.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams nameParams = new LayoutParams(
                0, LayoutParams.MATCH_PARENT, 4
        );
        LayoutParams descrParams = new LayoutParams(
                0, LayoutParams.MATCH_PARENT, 6
        );

        this.addView(mName, nameParams);
        this.addView(mDescription, descrParams);
    }
	/**
	* set name of this ListTripItem
	*/
    public void setName(String name){
        mName   .setText(name);
    }
	/**
	* set desription of this ListTripItem
	*/
    public void setDescription(String description){
        if(description.length() > 20){
            addMarquee(mDescription);
        }
        mDescription    .setText(description);
    }
    public void setInnerPadding(int left, int top, int right, int bottom, int spacing){
        mName           .setPadding(left, top, (spacing / 2), bottom);
        mDescription    .setPadding((spacing / 2), top, right, bottom);
    }
    public void setInnerPadding(int padding, int spacing){
        this.setInnerPadding(padding, padding, padding, padding, spacing);
    }

    private void addMarquee(TextView tv){
        tv  .setMarqueeRepeatLimit(-1);
        tv  .setHorizontalFadingEdgeEnabled(true);
        tv  .setFadingEdgeLength(5);
        tv  .setHorizontallyScrolling(true);
        tv  .setSingleLine(true);
        tv  .setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv  .setSelected(true);
    }

}
