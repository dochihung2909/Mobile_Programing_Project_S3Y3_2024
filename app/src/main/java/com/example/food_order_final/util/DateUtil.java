package com.example.food_order_final.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date timestampToDate(String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        try {
            return dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToTimestamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return dateFormat.format(date);
    }
}
