package com.aslan.baselibrary.http.error;

import android.content.Context;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.Maybe;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunctionMaybe<T> extends BaseHttpErrorFunction<Maybe<T>> {

  public HttpErrorFunctionMaybe(Context context) {
    super(context);
  }

  @Override
  public Maybe<T> error(DataError ex) {
    return Maybe.error(ex);
  }
}
