package com.aslan.baselibrary.http.error;

import android.content.Context;
import com.aslan.baselibrary.http.BaseError;
import io.reactivex.Completable;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionCompletable<T> extends BaseHttpErrorFunction<Completable> {

  public HttpErrorFunctionCompletable(Context context) {
    super(context);
  }

  @Override
  Completable error(BaseError ex) {
    return Completable.error(ex);
  }
}
