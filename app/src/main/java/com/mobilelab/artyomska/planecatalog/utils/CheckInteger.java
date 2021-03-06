package com.mobilelab.artyomska.planecatalog.utils;

/**
 * Created by Artyomska on 11/20/2017.
 */

public class CheckInteger
{
    public static boolean isInteger(String s, int radix)
    {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
