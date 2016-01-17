package kr.co.korea.util;

/**
 * Created by kjs on 2016-01-15.
 */
public class StringUtils {
    public static String nullToWhiteSpace(String value){
        if(value == null){
            value   =   "";
        }

        return value;
    }
}
