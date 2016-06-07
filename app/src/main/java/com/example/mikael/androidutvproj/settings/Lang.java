package com.example.mikael.androidutvproj.settings;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public enum Lang{
    ENGLISH("English", "en"),
    SWEDISH("Swedish", "sv");
    private String mName;
    private String mLangCode;
    Lang(String name, String langCode){
        mName = name;
        mLangCode = langCode;
    }
    public static Lang findByIdx(int idx){
        Lang[] langarr = values();
        return langarr[idx];
    }
    public String getName(){return mName;}
    public String getLangCode(){return mLangCode;}
    public static Lang getByName(String name){
        for(Lang lang : Lang.values()){
            if(lang.mName.equals(name))
                return lang;
        }
        return null;
    }
    /**
     * get Lang from lang code
     * @param langCode langcode of desired Lang
     * @return Lang of param langcode<br>null if langcode does not correspond to any Lang
     */
    public static Lang getByLangCode(String langCode){
        for(Lang lang : Lang.values()){
            if(lang.mLangCode.equals(langCode))
                return lang;
        }
        return null;
    }
}


