package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.exception.BusinessException;
import com.aslan.baselibrary.exception.TokenException;
import com.aslan.baselibrary.http.IHttpBean;
import io.reactivex.functions.Function;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public abstract class BaseResponseFunction<T, R> implements Function<IHttpBean<T>, R> {

  private Context context;

  public BaseResponseFunction(Context context) {
    this.context = context;
  }

  @Override
  public R apply(IHttpBean<T> respone) throws Exception {
    if (respone.isTokenError()) {
      return error(new TokenException(respone.getCode(), respone.getMessage()));
    }

    if (respone.isSuccessful()) {
      return handleData(respone.getData());
    } else {
      return error(new BusinessException(respone.getCode(), respone.getMessage()));
    }
  }

  public abstract R error(@NonNull Exception ex);

  public abstract R handleData(@Nullable T item);
}
