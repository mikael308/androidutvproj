package com.example.mikael.androidutvproj.eventlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.event.Event;

/**
 * class used as listitem containing a <Code>Event</Code> instance
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.1
 *
 */
public class EventListItem extends LinearLayout {

    private int mDescriptionMaxLength = 20;
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
        init();
    }
    public EventListItem(Context context, String header){
        super(context);
        init();
        setHeader(header);
    }
    public EventListItem(Context context, String header, String description){
        super(context);
        init();
        setHeader(header);
        setDescription(description);
    }
    public EventListItem(Context context, Event e){
        super(context);
        init();
        setHeader(e.getApartment().getAddress());
        setDescription(e.getApartment().getDescription());
    }

    /**
     * initialize this Layout
     */
    private void init(){
        View item           = LinearLayout.inflate(getContext(), R.layout.layout_eventlist_item, null);
        mHeader             = (TextView) item.findViewById(R.id.header);

        mDescription        = (TextView) item.findViewById(R.id.description);
        mDescription        .setEms(mDescriptionMaxLength);

        addView(item);
    }

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

    @Override
    public boolean equals(Object o) {
        try{
            EventListItem item = (EventListItem) o;
            return (this.getHeader().equals(item.getHeader())
                && this.getDescription().equals(item.getDescription()));

        } catch(ClassCastException e){
            return false;
        }
    }

}
