package com.aslan.baselibrary.http.error;

import android.content.Context;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.Single;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionSingle<T> extends BaseHttpErrorFunction<Single<T>> {

  public HttpErrorFunctionSingle(Context context) {
    super(context);
  }

  @Override
  public Single<T> error(DataError ex) {
    return Single.error(ex);
  }
}
