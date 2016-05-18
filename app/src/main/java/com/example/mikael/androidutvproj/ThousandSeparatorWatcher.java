package com.example.mikael.androidutvproj;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Watcher adds thousandseparation on textcontent in EditText after text has changed
 * @author Mikael Holmbom
 * @version 1.0
 */
public class ThousandSeparatorWatcher implements TextWatcher{

    private final static String REGEX_THOUSAND_SEP = "(?=(?:[0-9]{3})+(?![0-9]))";

    private String THOUSAND_SEP;

    private EditText mEditText;


    public ThousandSeparatorWatcher(EditText editText, String separatorSymbol){
        mEditText = editText;
        THOUSAND_SEP = separatorSymbol;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        mEditText.removeTextChangedListener(this);

        String content = getRawViewContent();
        content = content.replaceAll(REGEX_THOUSAND_SEP, THOUSAND_SEP+"\0").trim();

        mEditText.setText(content);
        mEditText.setSelection(content.length());

        mEditText.addTextChangedListener(this);
    }

    /**
     * get raw textcontent from current EditText without formatting
     * @return
     */
    private String getRawViewContent(){

        return mEditText.getText().toString().replaceAll("[^0-9]", "");
    }
}
