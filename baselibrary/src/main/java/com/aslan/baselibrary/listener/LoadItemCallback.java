package com.aslan.baselibrary.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadItemCallback<T> {

  void onLoaded(@Nullable T response);

  void onDataNotAvailable(@NonNull BaseHttpError error);
}
