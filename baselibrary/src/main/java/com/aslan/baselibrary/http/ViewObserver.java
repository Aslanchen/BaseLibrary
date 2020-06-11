package com.aslan.baselibrary.http;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.aslan.baselibrary.listener.IBaseView;

import io.reactivex.disposables.Disposable;

/**
 * 和View挂钩
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class ViewObserver<T> extends BaseObserver<T> {

    private IBaseView iBaseView;

    public ViewObserver(IBaseView iBaseView) {
        super(iBaseView.getContext());
        this.iBaseView = iBaseView;
    }

    @CallSuper
    @Override
    public void onSubscribe(Disposable d) {
        iBaseView.showProgressBar();
        super.onSubscribe(d);
    }

    @CallSuper
    @Override
    public void onComplete() {
        super.onComplete();
        iBaseView.closeProgressBar();
    }

    @CallSuper
    @Override
    public void handleError(@NonNull BaseError e) {
        iBaseView.showToastMessage(e);
    }
}