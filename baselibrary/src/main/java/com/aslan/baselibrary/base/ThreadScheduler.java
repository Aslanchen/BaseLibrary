package com.aslan.baselibrary.base;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Rxjava 线程
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
class ThreadScheduler {
    <T> ObservableTransformer<T, T> disc() {
        return upstream -> upstream.subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread());
    }

    <T> ObservableTransformer<T, T> network() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
