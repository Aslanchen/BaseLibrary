package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.base.DataError;
import io.reactivex.Completable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionCompletable<T> extends BaseResponseFunction<T, Completable> {

  public ResponseFunctionCompletable(Context context) {
    super(context);
  }

  @Override
  public Completable error(@NonNull DataError ex) {
    return Completable.error(ex);
  }

  @Override
  public Completable handleData(@Nullable T item) {
    return Completable.complete();
  }
}
