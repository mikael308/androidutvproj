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
    /**
     * this header
     */
    private TextView mHeader;
    /**
     * this description
     */
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

    /**
     * set name of this ListTripItem
     * @param header new header value
     * @return
     */
    public EventListItem setHeader(String header) {
        mHeader.setText(header);
        return this;
    }

    /**
     *
     * set desription of this ListTripItem
     * @param description new description value
     * @return this EventListItem
     */
    public EventListItem setDescription(String description){
        if(description.length() > mDescriptionMaxLength) {
            addMarquee(mDescription);
        }
        mDescription.setText(description);
        return this;
    }

    /**
     * get this header text
     * @return header text
     */
    public String getHeader(){
        return mHeader.getText().toString();
    }

    /**
     * get this description text
     * @return description text
     */
    public String getDescription(){
        return mDescription.getText().toString();
    }

    /**
     * add marquee to textview: rolling text animation
     * @param tv textView to add marquee to
     */
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
