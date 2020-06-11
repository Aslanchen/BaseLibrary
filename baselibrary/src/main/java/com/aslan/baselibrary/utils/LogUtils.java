package com.aslan.baselibrary.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.Flattener2;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志模块
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/5/26
 */
public final class LogUtils {
    private LogUtils() {
    }

    private static SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
            Locale.getDefault());

    /**
     * 配置
     *
     * @param tag        标签
     * @param logLevel   {@link LogLevel}
     * @param folderPath 本地日志文件夹路径
     */
    public static void config(@NonNull String tag, int logLevel, @Nullable String folderPath) {
        LogConfiguration config = new LogConfiguration.Builder()
                .tag(tag)
                .logLevel(logLevel)
                .build();

        //android控制台
        Printer androidPrinter = new AndroidPrinter();

        if (TextUtils.isEmpty(folderPath)) {
            XLog.init(config, androidPrinter);
        } else {
            //本地日志
            Printer filePrinter = new FilePrinter
                    .Builder(folderPath)
                    .fileNameGenerator(new DateFileNameGenerator())
                    .backupStrategy(new FileSizeBackupStrategy(5 * 1024 * 1024))
                    .cleanStrategy(new FileLastModifiedCleanStrategy(3 * 24 * 60 * 60 * 1000))
                    .flattener(new Flattener2() {
                        @Override
                        public CharSequence flatten(long timeMillis, int logLevel, String tag,
                                                    String message) {
                            return String.format("%s %s/%s: %s", mFormat.format(timeMillis),
                                    LogLevel.getShortLevelName(logLevel),
                                    tag, message);
                        }
                    })
                    .build();
            XLog.init(config, androidPrinter, filePrinter);
        }
    }

    public static void v(String msg) {
        XLog.v(msg);
    }

    public static void v(String format, Object... args) {
        XLog.v(format, args);
    }

    public static void v(String msg, Throwable tr) {
        XLog.v(msg, tr);
    }

    public static void d(String msg) {
        XLog.d(msg);
    }

    public static void d(String format, Object... args) {
        XLog.d(format, args);
    }

    public static void d(String msg, Throwable tr) {
        XLog.d(msg, tr);
    }

    public static void i(String msg) {
        XLog.i(msg);
    }

    public static void i(String format, Object... args) {
        XLog.i(format, args);
    }

    public static void i(String msg, Throwable tr) {
        XLog.i(msg, tr);
    }

    public static void w(String msg) {
        XLog.w(msg);
    }

    public static void w(String format, Object... args) {
        XLog.w(format, args);
    }

    public static void w(String msg, Throwable tr) {
        XLog.w(msg, tr);
    }

    public static void e(String msg) {
        XLog.e(msg);
    }

    public static void e(String format, Object... args) {
        XLog.e(format, args);
    }

    public static void e(String msg, Throwable tr) {
        XLog.e(msg, tr);
    }
}
