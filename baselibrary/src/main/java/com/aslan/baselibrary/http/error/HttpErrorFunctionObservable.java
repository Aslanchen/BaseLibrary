package com.aslan.baselibrary.http.error;

import android.content.Context;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.Observable;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionObservable<T> extends BaseHttpErrorFunction<Observable<T>> {

  public HttpErrorFunctionObservable(Context context) {
    super(context);
  }

  @Override
  public Observable<T> error(DataError ex) {
    return Observable.error(ex);
  }
}
