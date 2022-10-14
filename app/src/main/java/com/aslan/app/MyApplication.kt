package com.aslan.app

import androidx.multidex.MultiDexApplication
import com.aslan.baselibrary.utils.FileUtil
import com.aslan.baselibrary.utils.LogUtils
import com.elvishew.xlog.LogLevel

/**
 *
 *
 * @author chenhengfei(Aslanchen)
 * @date 2021/8/3
 */
class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
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