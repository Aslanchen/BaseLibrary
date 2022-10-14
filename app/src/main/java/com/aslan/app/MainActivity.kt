package com.aslan.app

import android.os.Bundle
import com.aslan.app.databinding.ActivityMainBinding
import com.aslan.baselibrary.base.DataTransformerCompletable
import com.aslan.baselibrary.base.VBBaseActivity
import com.aslan.baselibrary.http.observer.DataCompletableObserver
import com.aslan.baselibrary.utils.FileUtil
import com.aslan.baselibrary.utils.LogUtils
import com.elvishew.xlog.LogLevel
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivity : VBBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun iniBundle(bundle: Bundle) {}
    override fun iniView() {}

    override fun iniListener() {}

    override fun iniData() {
        initLog()
    }

    private fun initLog() {
        LogUtils.config(
            "App",
            if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO,
            FileUtil.getLog(this).path
        )
    }

    private fun rxjava() {
        Completable.complete()
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformerCompletable(mBaseView = this))
            .subscribe(object : DataCompletableObserver(this) {
                override fun handleSuccess() {
                    showToastMessage("hello")
                }
            })
    }
}