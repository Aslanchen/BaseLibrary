package com.aslan.app

import android.os.Bundle
import com.aslan.app.databinding.ActivityMainBinding
import com.aslan.baselibrary.base.DataCompletableTransformer
import com.aslan.baselibrary.base.VBBaseActivity
import com.aslan.baselibrary.exception.BusinessException
import com.aslan.baselibrary.http.observer.DataCompletableObserver
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : VBBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun iniBundle(bundle: Bundle) {
    }

    override fun iniView() {
    }

    override fun iniListener() {
        mViewBinding.bt.setOnClickListener {
            Completable.create {
                Thread.sleep(1000)
                it.tryOnError(BusinessException("12121", "12121"))
            }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .compose(DataCompletableTransformer(mBaseView = this))
                .subscribe(object : DataCompletableObserver(this) {
                    override fun handleSuccess() {
                        showToastMessage("hello")
                    }
                })
        }
    }

    override fun iniData() {
    }
}