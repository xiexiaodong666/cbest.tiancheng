package com.welfare.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
public class StringUtil {
    private StringUtil(){

    }
    static final Pattern START_WITH_NUMBER_PATTERN = Pattern.compile("[0-9].*");

    public static boolean startsWithNumber(String str) {
        Matcher isNum = START_WITH_NUMBER_PATTERN.matcher(str.charAt(0)+"");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取下一个英文字母
     * @param en
     * @return
     */
    public static String getNextUpEn(String en){
        if(en==null || en.equals(""))
            return "A";
        char lastE = 'Z';
        int lastEnglish = (int)lastE;
        char[] c = en.toCharArray();
        if(c.length>1){
            return null;
        }else{
            int now = (int)c[0];
            if(now >= lastEnglish)
                return null;
            char uppercase = (char)(now+1);
            return String.valueOf(uppercase);
        }
    }
}
