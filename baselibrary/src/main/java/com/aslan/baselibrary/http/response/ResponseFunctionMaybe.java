package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.Maybe;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionMaybe<T> extends BaseResponseFunction<T, Maybe<T>> {

  public ResponseFunctionMaybe(Context context) {
    super(context);
  }

  @Override
  public Maybe<T> error(@NonNull DataError ex) {
    return Maybe.error(ex);
  }

  @Override
  public Maybe<T> handleData(@Nullable T item) {
    if (item == null) {
      return Maybe.empty();
    } else {
      return Maybe.just(item);
    }
  }
}
