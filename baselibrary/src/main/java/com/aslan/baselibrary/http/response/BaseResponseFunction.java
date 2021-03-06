package com.aslan.baselibrary.http.response;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.base.DataError;
import com.aslan.baselibrary.http.EventTokenError;
import com.aslan.baselibrary.http.IHttpBean;
import io.reactivex.functions.Function;
import org.greenrobot.eventbus.EventBus;

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
      EventBus.getDefault().post(new EventTokenError(respone.getCode()));
      return error(new DataError(DataError.ERROR_HTTP_TOKEN_ERROR,
          context.getString(com.aslan.baselibrary.R.string.error_net_token_error)));
    }

    if (respone.isSuccessful()) {
      return handleData(respone.getData());
    } else {
      return error(new DataError(respone.getCode(), respone.getMessage()));
    }
  }

  public abstract R error(@NonNull DataError ex);

  public abstract R handleData(@Nullable T item);
}
