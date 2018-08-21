package com.aslan.baselibrary.listener;

import android.support.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import java.util.List;

public interface LoadListCallback<T> {

  void onLoaded(@NonNull List<T> respones);

  void onDataNotAvailable(BaseHttpError error);
}
