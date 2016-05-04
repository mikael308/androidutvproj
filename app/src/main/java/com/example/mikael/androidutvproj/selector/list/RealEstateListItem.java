package com.example.mikael.androidutvproj.selector.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikael.androidutvproj.R;
import com.example.mikael.androidutvproj.dao.RealEstate;

/**
 * class used as listitem containing a RealEstate instance<br>
 *     listitem contains of
 *     <ul>
 *         <li>header</li>
 *         <li>subtitle/li>
 *     </ul>
 *
 * @author Mikael Holmbom - miho1202
 * @version 1.1
 * @see RealEstate
 */
public class RealEstateListItem extends LinearLayout {

    private int mSubtitleMaxLength = 20;
    /**
     * this header
     */
    private TextView mHeader;
    /**
     * this subtitle
     */
    private TextView mSubtitle;

    public RealEstateListItem(Context context) {
        super(context);
        init();
    }
    public RealEstateListItem(Context context, String header){
        super(context);
        init();
        setHeader(header);
    }
    public RealEstateListItem(Context context, String header, String subtitle){
        super(context);
        init();
        setHeader(header);
        setSubtitle(subtitle);
    }
    public RealEstateListItem(Context context, RealEstate realEstate){
        super(context);
        init();
        setHeader(realEstate.getAddress());
        setSubtitle(realEstate.getDescription());
    }

    /**
     * initialize this Layout
     */
    private void init(){
        View item           = LinearLayout.inflate(getContext(), R.layout.layout_eventlist_item, null);
        mHeader             = (TextView) item.findViewById(R.id.header);
        mSubtitle           = (TextView) item.findViewById(R.id.subtitle);

        addView(item);
    }

    /**
     * set name of this ListTripItem
     * @param header new header value
     * @return
     */
    public RealEstateListItem setHeader(String header) {
        mHeader.setText(header);
        return this;
    }

    /**
     *
     * set subtitle of this ListTripItem
     * @param subtitle new value
     * @return this EventListItem
     */
    public RealEstateListItem setSubtitle(String subtitle){
        if(subtitle.length() > mSubtitleMaxLength) {
            addMarquee(mSubtitle);
        }
        mSubtitle.setText(subtitle);
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
     * get this subtitle string
     * @return subtitle string
     */
    public String getSubtitle(){
        return mSubtitle.getText().toString();
    }

    /**
     * persist marquee to textview: rolling text animation
     * @param tv textView to persist marquee to
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
            RealEstateListItem item = (RealEstateListItem) o;
            return (this.getHeader().equals(item.getHeader())
                && this.getSubtitle().equals(item.getSubtitle()));

        } catch(ClassCastException e){
            return false;
        }
    }

}
