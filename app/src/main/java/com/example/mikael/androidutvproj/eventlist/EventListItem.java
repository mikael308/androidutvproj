package com.example.mikael.androidutvproj.eventlist;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * class used as listitem containing a <Code>Event</Code> instance
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.1
 *
 */
public class EventListItem extends LinearLayout {

    private int mDescriptionMaxLength = 20;
    private TextView mName;
    private TextView mDescription;

    public EventListItem(Context context) {
        super(context);
        mName           = new TextView(getContext());
        mDescription    = new TextView(getContext());
        mDescription.setEms(mDescriptionMaxLength);

        init();
    }

    /**
     * initialize this Layout
     */
    private void init(){
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutParams nameParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 4
        );
        LayoutParams descrParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 6
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
        if(description.length() > mDescriptionMaxLength){
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
