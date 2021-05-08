package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.http.BaseError;
import io.reactivex.Single;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionSingle<T> extends BaseResponseFunction<T, Single<T>> {

  public ResponseFunctionSingle(Context context) {
    super(context);
  }

  @Override
  public Single<T> error(@NonNull BaseError ex) {
    return Single.error(ex);
  }

  @Override
  public Single<T> handleData(@Nullable T item) {
    if (item == null) {
      return Single.error(new NullPointerException("respone data is empty"));
    } else {
      return Single.just(item);
    }
  }
}
