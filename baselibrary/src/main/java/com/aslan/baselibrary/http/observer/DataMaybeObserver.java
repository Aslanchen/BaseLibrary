package com.aslan.baselibrary.http.observer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.CancellationException;

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class DataMaybeObserver<T> implements MaybeObserver<T> {

  private Context context;

  public DataMaybeObserver(Context context) {
    this.context = context;
  }

  @Override
  public void onError(@NonNull Throwable e) {
    if (e instanceof DataError) {
      handleError((DataError) e);
    } else if (e instanceof CancellationException) {
      //Rxjava绑定生命周期后，会触发此异常
      return;
    } else {
      handleError(new DataError(e));
    }
  }

  @Override
  public void onSubscribe(@NonNull Disposable d) {

  }

  @Override
  public void onComplete() {
    handleSuccess(null);
  }

  @Override
  public void onSuccess(@NonNull T t) {
    handleSuccess(t);
  }

  public abstract void handleError(@NonNull DataError e);

  public abstract void handleSuccess(@Nullable T t);
}