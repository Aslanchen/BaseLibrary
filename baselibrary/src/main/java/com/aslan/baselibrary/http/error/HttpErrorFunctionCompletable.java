package com.aslan.baselibrary.http.error;

import android.content.Context;
import io.reactivex.Completable;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionCompletable extends BaseHttpErrorFunction<Completable> {

  public HttpErrorFunctionCompletable(Context context) {
    super(context);
  }

  @Override
  public Completable error(Exception ex) {
    return Completable.error(ex);
  }
}
