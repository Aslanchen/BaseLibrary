package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.http.BaseError;
import io.reactivex.Flowable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionFlowable<T> extends BaseResponseFunction<T, Flowable<T>> {

  public ResponseFunctionFlowable(Context context) {
    super(context);
  }

  @Override
  Flowable<T> error(@NonNull BaseError ex) {
    return Flowable.error(ex);
  }

  @Override
  Flowable<T> handleData(@Nullable T item) {
    if (item == null) {
      return Flowable.error(new NullPointerException("respone data is empty"));
    } else {
      return Flowable.just(item);
    }
  }
}
