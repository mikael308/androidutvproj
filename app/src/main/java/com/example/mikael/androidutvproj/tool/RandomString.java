package com.example.mikael.androidutvproj.tool;

import java.util.Random;

/**
 * @author Mikael Holmbom
 * @version 1.0
 */
public class RandomString {

    /**
     * get a random alpha string of length n
     * @param n
     * @return
     */
    public static String getRandomAlphaString(int n){
        String alpha = "abcdefghijklmnopqestuvwxyzABCDEFGHIJKLMNOPQQRSTUVWXYZ";
        Random rand = new Random();
        String str = "";
        for(int j = 0; j < n; j++){
            str += alpha.charAt(rand.nextInt(alpha.length()));
        }
        return str;
    }

}
