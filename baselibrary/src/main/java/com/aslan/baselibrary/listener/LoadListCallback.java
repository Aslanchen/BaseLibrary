package com.aslan.baselibrary.listener;

import androidx.annotation.NonNull;
import com.aslan.baselibrary.http.BaseError;
import java.util.List;

public interface LoadListCallback<T> {

  void onLoaded(@NonNull List<T> responses);

  void onDataNotAvailable(@NonNull BaseError error);
}
