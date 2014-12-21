/*
 * Author: Hasib Al Muhaimin.
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 */

package com.sadakhata.banglatoenglishrupantor;

public class Rupantor{
    public static String convert(String str)
    {


        String find[]   = {"অ", "আ", "ই", "ঈ", "উ", "ঊ", "ঋ", "এ", "ঐ", "ও", "ঔ"};
        String replace[] =  {"o",  "a", "i", "i", "u", "u", "rri","e", "oi", "o", "ou"};

        for(int i=0; i<find.length; i++)
        {
            str = str.replace(find[i], replace[i]);
        }

        find = new String[]{"া", "ি", "ী", "ু", "ূ", "ৃ", "ে", "ৈ", "ো", "ৌ"};

        replace = new String[]{"a", "i", "i", "u", "u", "rri", "e", "oi", "o", "ou"};

        for(int i=0; i<find.length; i++)
        {
            str = str.replace(find[i], replace[i]);
        }


        find = new String[]{"ক", "খ", "গ", "ঘ", "ঙ", "চ", "ছ", "জ", "ঝ", "ঞ", "ট", "ঠ",
               "ড", "ঢ","ণ", "ত", "থ", "দ", "ধ", "ন", "প", "ফ", "ব", "ভ", "ম", "য", "র",
               "ল", "শ", "ষ", "স", "হ", "ড়", "ঢ়", "য়", "ং", "ঃ", "ঁ"};

        replace = new String[]{"k", "kh", "g", "gh", "ng", "c", "ch", "j", "jh", "ng", "t", "th",
                "d", "dh", "n", "t", "th", "d", "dh", "n", "p", "f", "b", "v", "m", "j", "r",
                "l", "sh", "s", "s", "h", "r", "rh", "y", "ng", ":", ""};

        for(int i=0; i<find.length; i++)
        {
            str = str.replace(find[i], replace[i]);
        }

        find = new String[]{"ব়", "ড়", "ঢ়", "য়"};
        replace = new String[]{"r", "r", "rh", "y"};

        for(int i=0; i<find.length; i++)
        {
            str = str.replace(find[i], replace[i]);
        }


        find = new String[]{"্"};
        replace = new String[]{""};

        for(int i=0; i<find.length; i++)
        {
            str = str.replace(find[i], replace[i]);
        }

        return  str;
    }


}