package com.example.mikael.androidutvproj;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView used for numbers displayed with thousand seperator
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class TextViewNumber extends TextView {

    /**
     * text/symbol used for thousand seperating
     */
    private String mThousand_sep = "";
    /**
     * text/symbol used as decimal seperator
     */
    private String mDecimal_sep = "";

    private String mUnitSymbol = "";

    public TextViewNumber(Context context) {
        super(context);
        initListeners();
    }

    public TextViewNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initListeners();
    }

    public TextViewNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initListeners();
    }
    /**
     * initialize this attributes from xml attributes
     * @param attrs
     */
    private void init(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewNumber);

        mThousand_sep   = ta.getString(R.styleable.TextViewNumber_thousandSeparatorSymbol);
        mDecimal_sep    = ta.getString(R.styleable.TextViewNumber_decimalSeparatorSymbol);
        mUnitSymbol     = ta.getString(R.styleable.TextViewNumber_valueUnit);

        ta.recycle();
    }
    /**
     * init changeListener
     */
    private void initListeners(){
        mThousandSeparator = new ThousandSeparator(this, mThousand_sep, mDecimal_sep, mUnitSymbol);
    }

    /**
     * get text without formatting
     * @return
     */
    public String getRawText(){
        String content = getText().toString();
        return ThousandSeparator.unFormat(content, getContext().getString(R.string.decimal_sep), mUnitSymbol);
    }

    public void setText(double val){
        super.setText(String.valueOf(val));
    }
}
