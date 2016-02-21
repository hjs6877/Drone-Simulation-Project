package kr.co.korea.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kjs on 2016-02-17.
 */
public class DateUtils {
    public static String getCurrentDateDefaultFormatted(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);

        return dateStr;
    }

    public static String getCurrentDateForFileName(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = simpleDateFormat.format(date);

        return dateStr;
    }
}
