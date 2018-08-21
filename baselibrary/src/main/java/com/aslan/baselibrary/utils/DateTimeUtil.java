package com.aslan.baselibrary.utils;

/**
 * 日期时间操作类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public final class DateTimeUtil {

    /**
     * 时间差
     */
    private static long timeoffset = 0;

    private DateTimeUtil() {

    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis() + timeoffset;
    }
}
