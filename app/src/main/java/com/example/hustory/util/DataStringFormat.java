package com.example.hustory.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataStringFormat {
    public static final int _SEC = 60;
    public static final int _MIN = 60;
    public static final int _HOUR = 24;
    public static final int _DAY = 30;
    public static final int _MONTH = 12;

    public static String makeStringComma(String str) {
        if(str.length() == 0) {
            return "";
        }
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

    // String 데이터를 년/월/일으로 바꿔주는 함수
    public static String DataFormat(String dataString) {
        String resultString = null;

        resultString = dataString.substring(0,4).toString() + "년";
        resultString = dataString.substring(4,6).toString() + "월";
        resultString = dataString.substring(6,8).toString() + "일";

        return resultString;
    }

    public static String CreateDataWithCheck(String dataString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;

        try {
            date = format.parse(dataString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if(diffTime < _SEC) {
            msg = "방금 전";
        }
        else if((diffTime /= _SEC) < _MIN) {
            msg = diffTime + "분 전";
        }
        else if((diffTime /= _MIN) < _HOUR) {
            msg = diffTime + "시간 전";
        }
        else if((diffTime /= _HOUR) < _DAY) {
            msg = diffTime + "일 전";
        }
        else if((diffTime /= _DAY) < _MONTH) {
            msg = diffTime + "달 전";
        }
        else {
            format = new SimpleDateFormat("yyyy-MM-dd");
            msg = format.format(date);
        }

        return msg;
    }

}
