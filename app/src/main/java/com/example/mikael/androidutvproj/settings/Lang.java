package com.example.mikael.androidutvproj.settings;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
enum Lang{
    ENGLISH("English"),
    SWEDISH("Swedish");
    private String mName;
    Lang(String name){
        mName = name;
    }
    public static Lang findByIdx(int idx){
        Lang[] langarr = values();
        return langarr[idx];
    }
    public String getName(){return mName;}
}


