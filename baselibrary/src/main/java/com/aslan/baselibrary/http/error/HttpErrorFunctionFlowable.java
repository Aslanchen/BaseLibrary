package com.aslan.baselibrary.http.error;

import android.content.Context;
import io.reactivex.Flowable;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionFlowable<T> extends BaseHttpErrorFunction<Flowable<T>> {

  public HttpErrorFunctionFlowable(Context context) {
    super(context);
  }

  @Override
  public Flowable<T> error(Exception ex) {
    return Flowable.error(ex);
  }
}
