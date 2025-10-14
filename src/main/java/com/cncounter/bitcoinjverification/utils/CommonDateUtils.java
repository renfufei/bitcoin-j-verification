package com.cncounter.bitcoinjverification.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonDateUtils {

    public static final String yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";

    // 当前时间
    public static final String curTimeStr() {
        return dateToStr(new Date());

    }
    // 当前时间
    public static final String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd_HHmmss, Locale.CHINA);
        return format.format(date);

    }
}
