package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.http.BaseError;
import io.reactivex.Observable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionObservable<T> extends BaseResponseFunction<T, Observable<T>> {

  public ResponseFunctionObservable(Context context) {
    super(context);
  }

  @Override
  public Observable<T> error(@NonNull BaseError ex) {
    return Observable.error(ex);
  }

  @Override
  public Observable<T> handleData(@Nullable T item) {
    if (item == null) {
      return Observable.error(new NullPointerException("respone data is empty"));
    } else {
      return Observable.just(item);
    }
  }
}
