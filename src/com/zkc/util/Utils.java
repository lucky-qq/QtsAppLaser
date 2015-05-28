package com.zkc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IT on 2015-04-22.
 */
public class Utils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeNow() {
        return format.format(new Date());
    }
}
