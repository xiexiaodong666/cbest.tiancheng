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
}
