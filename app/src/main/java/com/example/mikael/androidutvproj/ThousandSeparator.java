package com.example.mikael.androidutvproj;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Watcher adds thousandseparation on textcontent in EditText after text has changed
 * @author Mikael Holmbom
 * @version 1.0
 */
public class ThousandSeparator implements TextWatcher{

    private final static String REGEX_THOUSAND_SEP = "(?=(?:[0-9]{3})+(?![0-9]))";

    private final static String REGEX_DIGITS_ONLY = "[^(0-9)%s]";

    private final static String FORMAT_DIGIT_UNIT = "%s %s";

    private String THOUSAND_SEP = null;

    private String DECIMAL_SEP = null;

    private String UNIT_SYMBOL = null;

    private TextView mTextView = null;


    public ThousandSeparator(TextView textView, String separatorSymbol){
        mTextView = textView;
        THOUSAND_SEP = separatorSymbol;
    }

    public ThousandSeparator(TextView textView, String thousandSeparator, String decimalSeparator){
        mTextView       = textView;
        THOUSAND_SEP    = thousandSeparator;
        DECIMAL_SEP     = decimalSeparator;
    }

    /**
     * to use without unit symbol, set as null
     * @param textView
     * @param thousandSeparator
     * @param decimalSeparator
     * @param unitSymbol set as null to display value without symbol
     */
    public ThousandSeparator(TextView textView, String thousandSeparator, String decimalSeparator, String unitSymbol){
        mTextView       = textView;
        THOUSAND_SEP    = thousandSeparator;
        DECIMAL_SEP     = decimalSeparator;
        UNIT_SYMBOL     = unitSymbol;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        mTextView.removeTextChangedListener(this);

        String content = mTextView.getText().toString();

        if (DECIMAL_SEP != null){
            content = unFormat(content, DECIMAL_SEP, UNIT_SYMBOL);
            content = format(content, THOUSAND_SEP, DECIMAL_SEP);
        } else {
            content = unFormat(content);
            content = format(content, THOUSAND_SEP);
        }

        if (UNIT_SYMBOL != null && ! UNIT_SYMBOL.isEmpty()){
            content = String.format(FORMAT_DIGIT_UNIT, content, UNIT_SYMBOL);
        }

        mTextView.setText(content);
        try{
            ((EditText) mTextView).setSelection(mTextView.getText().length());

        } catch(ClassCastException e){

        }

        mTextView.addTextChangedListener(this);
    }

    /**
     * get string as decimalnumber without thousand separator format
     * @param s text to format
     * @param decimalSeparator decimal symbol
     * @return
     */
    public static String unFormat(String s, String decimalSeparator, String unitSymbol){
        String dec_sep ="(";
        if(decimalSeparator.equals("."))
            dec_sep+="\\";
        dec_sep += decimalSeparator + ")";
        String regex = String.format(REGEX_DIGITS_ONLY, dec_sep, unitSymbol);
        return s.replaceAll(regex, "");
    }

    /**
     * get raw text from s without thousand separator format
     * @return
     */
    public static String unFormat(String s) {
        return s.replaceAll(String.format(REGEX_DIGITS_ONLY, ""), "");
    }

    /**
     * format a integer, whole number, to thousand separator format
     * @param s text to format
     * @param thousandSeparator symbol
     * @return
     */
    public static String format(String s, String thousandSeparator){
        return s.replaceAll(REGEX_THOUSAND_SEP, thousandSeparator+"\0").trim();
    }
    public static String format(int integer, String thousandSeparator){
        return format(String.valueOf(integer), thousandSeparator);
    }
    public static String format(int integer, String thousandSeparator, String unitSymbol){
        return format(String.valueOf(integer), thousandSeparator, unitSymbol);
    }

    /**
     * format a decimal number to thousand separator format
     * @param s text to format
     * @param thousandSeparator symbol
     * @param decimalSeparator symbol
     * @return
     */
    public static String format(String s, String thousandSeparator, String decimalSeparator){
        return format(s, thousandSeparator, decimalSeparator, null);
    }

    public static String format(String s, String thousandSeparator, String decimalSeparator, String unitSymbol){
        String dec_sep = decimalSeparator;

        String decimals = "";
        int firstDec = s.indexOf(dec_sep);
        if (firstDec >= 0){
            decimals = s.substring(firstDec);
            s = s.substring(0, firstDec);
        }

        if(unitSymbol != null && ! unitSymbol.isEmpty()){
            s = String.format(FORMAT_DIGIT_UNIT, s, unitSymbol);
        }

        return format(s, thousandSeparator) + decimals;
    }

    public static String format(Double d, String thousandSeparator, String decimalSeparator){
        return format(String.valueOf(d), thousandSeparator, decimalSeparator);
    }
    public static String format(Double d, String thousandSeparator, String decimalSeparator, String unitSymbol){
        return format(String.valueOf(d), thousandSeparator, decimalSeparator, unitSymbol);
    }
}
