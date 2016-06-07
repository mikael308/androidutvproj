package com.example.mikael.androidutvproj;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * EditText used for numbers displayed with thousand seperator
 *
 * @author Mikael Holmbom
 * @version 1.0
 */
public class EditTextNumber extends EditText {



    private ThousandSeparator mThousandSeparator;

    private ThousandSeparator mThousandSeparatorOnFocus;
    /**
     * text/symbol used for thousand seperating
     */
    private String mThousand_sep = "";
    /**
     * text/symbol used as decimal seperator
     */
    private String mDecimal_sep = "";

    private String mUnitSymbol = "";

    public EditTextNumber(Context context) {
        super(context);
        mThousand_sep   = getContext().getString(R.string.thousand_sep);
        mDecimal_sep    =  getContext().getString(R.string.decimal_sep);
        initListeners();
    }

    public EditTextNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initListeners();
    }

    public EditTextNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initListeners();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused){
            removeTextChangedListener(mThousandSeparator);
            String text = getText().toString();
            text = ThousandSeparator.unFormat(text);
            text = ThousandSeparator.format(text, mThousand_sep, mDecimal_sep);
            setText(text);
            addTextChangedListener(mThousandSeparatorOnFocus);

        } else {
            removeTextChangedListener(mThousandSeparatorOnFocus);
            String text = getText().toString();
            text = ThousandSeparator.unFormat(text);
            text = ThousandSeparator.format(text, mThousand_sep, mDecimal_sep, mUnitSymbol);
            setText(text);
            addTextChangedListener(mThousandSeparator);

        }

    }

    /**
     * initialize this attributes from xml attributes
     * @param attrs
     */
    private void init(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.EditTextNumber);

        mThousand_sep   = ta.getString(R.styleable.EditTextNumber_thousandSeparatorSymbol);
        mDecimal_sep    = ta.getString(R.styleable.EditTextNumber_decimalSeparatorSymbol);
        mUnitSymbol     = ta.getString(R.styleable.EditTextNumber_valueUnit);

        ta.recycle();
    }

    /**
     * init changeListener
     */
    private void initListeners(){
        mThousandSeparator          = new ThousandSeparator(this, mThousand_sep, mDecimal_sep, mUnitSymbol);
        mThousandSeparatorOnFocus   = new ThousandSeparator(this, mThousand_sep, mDecimal_sep);
        addTextChangedListener(mThousandSeparator);
    }

    /**
     * get text without formatting
     * @return
     */
    public String getRawText(){
        String content = getText().toString();
        return ThousandSeparator.unFormat(content, mDecimal_sep, mUnitSymbol);
    }

}
