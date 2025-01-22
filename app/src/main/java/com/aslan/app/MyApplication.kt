package com.aslan.app

import android.app.Application
import androidx.multidex.MultiDex
import com.aslan.baselibrary.utils.FileUtil
import com.aslan.baselibrary.utils.LogUtils
import com.elvishew.xlog.LogLevel

/**
 *
 *
 * @author chenhengfei(Aslanchen)
 * @date 2021/8/3
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        initLog()
    }

    private fun initLog() {
        LogUtils.config(
            "App",
            if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO,
            FileUtil.getLog(this).path
        )
    }
}