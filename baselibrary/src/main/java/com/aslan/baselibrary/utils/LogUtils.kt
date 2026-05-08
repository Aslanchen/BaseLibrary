package com.aslan.baselibrary.utils

import android.text.TextUtils
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 日志模块
 * 
 * @author Aslan chenhengfei@yy.com
 * @date 2020/5/26
 */
object LogUtils {
    const val DEFAULT_LOG_FILE_MAX_SIZE = 50L * 1024 * 1024
    const val DEFAULT_LOG_FILE_MAX_BACKUP_INDEX = 1
    const val DEFAULT_LOG_FILE_MAX_ALIVE_TIME = 3L * 24 * 60 * 60 * 1000

    private val mFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    class Config private constructor(
        val tag: String,
        val logLevel: Int,
        val folderPath: String?,
        val maxLogFileSize: Long,
        val maxLogFileBackupIndex: Int,
        val maxLogFileAliveTime: Long
    ) {
        class Builder(
            private var tag: String,
            private var logLevel: Int,
            private var folderPath: String?
        ) {
            private var maxLogFileSize: Long = DEFAULT_LOG_FILE_MAX_SIZE
            private var maxLogFileBackupIndex: Int = DEFAULT_LOG_FILE_MAX_BACKUP_INDEX
            private var maxLogFileAliveTime: Long = DEFAULT_LOG_FILE_MAX_ALIVE_TIME

            fun setTag(tag: String): Builder = apply {
                this.tag = tag
            }

            fun setLogLevel(logLevel: Int): Builder = apply {
                this.logLevel = logLevel
            }

            fun setFolderPath(folderPath: String?): Builder = apply {
                this.folderPath = folderPath
            }

            fun setMaxLogFileSize(maxLogFileSize: Long): Builder = apply {
                this.maxLogFileSize = maxLogFileSize
            }

            fun setMaxLogFileBackupIndex(maxLogFileBackupIndex: Int): Builder = apply {
                this.maxLogFileBackupIndex = maxLogFileBackupIndex
            }

            fun setMaxLogFileAliveTime(maxLogFileAliveTime: Long): Builder = apply {
                this.maxLogFileAliveTime = maxLogFileAliveTime
            }

            fun build(): Config {
                return Config(
                    tag = tag,
                    logLevel = logLevel,
                    folderPath = folderPath,
                    maxLogFileSize = maxLogFileSize,
                    maxLogFileBackupIndex = maxLogFileBackupIndex,
                    maxLogFileAliveTime = maxLogFileAliveTime
                )
            }
        }
    }

    /**
     * 配置
     */
    fun config(config: Config) {
        val logConfiguration = LogConfiguration.Builder()
            .tag(config.tag)
            .logLevel(config.logLevel)
            .build()

        //android控制台
        val androidPrinter: Printer = AndroidPrinter()

        if (TextUtils.isEmpty(config.folderPath)) {
            XLog.init(logConfiguration, androidPrinter)
        } else {
            require(config.maxLogFileSize > 0) { "maxLogFileSize must be greater than 0." }
            require(config.maxLogFileBackupIndex >= 0 && config.maxLogFileBackupIndex != Int.MAX_VALUE) {
                "maxLogFileBackupIndex must be 0 or a positive value less than Int.MAX_VALUE."
            }
            require(config.maxLogFileAliveTime > 0) { "maxLogFileAliveTime must be greater than 0." }

            //本地日志
            val filePrinter: Printer? = FilePrinter.Builder(config.folderPath)
                .fileNameGenerator(DateFileNameGenerator())
                .backupStrategy(
                    FileSizeBackupStrategy2(
                        config.maxLogFileSize,
                        config.maxLogFileBackupIndex
                    )
                )
                .cleanStrategy(FileLastModifiedCleanStrategy(config.maxLogFileAliveTime))
                .flattener { timeMillis, logLevel, tag, message ->
                    String.format(
                        "%s %s/%s: %s", mFormat.format(timeMillis),
                        LogLevel.getShortLevelName(logLevel),
                        tag, message
                    )
                }
                .build()
            XLog.init(logConfiguration, androidPrinter, filePrinter)
        }
    }

    fun config(builder: Config.Builder) {
        config(builder.build())
    }

    fun v(msg: String?) {
        XLog.v(msg)
    }

    fun v(format: String?, vararg args: Any?) {
        XLog.v(format, *args)
    }

    fun v(msg: String?, tr: Throwable?) {
        XLog.v(msg, tr)
    }

    fun d(msg: String?) {
        XLog.d(msg)
    }

    fun d(format: String?, vararg args: Any?) {
        XLog.d(format, *args)
    }

    fun d(msg: String?, tr: Throwable?) {
        XLog.d(msg, tr)
    }

    fun i(msg: String?) {
        XLog.i(msg)
    }

    fun i(format: String?, vararg args: Any?) {
        XLog.i(format, *args)
    }

    fun i(msg: String?, tr: Throwable?) {
        XLog.i(msg, tr)
    }

    fun w(msg: String?) {
        XLog.w(msg)
    }

    fun w(format: String?, vararg args: Any?) {
        XLog.w(format, *args)
    }

    fun w(msg: String?, tr: Throwable?) {
        XLog.w(msg, tr)
    }

    fun e(msg: String?) {
        XLog.e(msg)
    }

    fun e(format: String?, vararg args: Any?) {
        XLog.e(format, *args)
    }

    fun e(msg: String?, tr: Throwable?) {
        XLog.e(msg, tr)
    }
}
