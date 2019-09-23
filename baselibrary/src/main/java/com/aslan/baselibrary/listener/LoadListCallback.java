package com.aslan.baselibrary.listener;

import androidx.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import java.util.List;

public interface LoadListCallback<T> {

  void onLoaded(@NonNull List<T> responses);

  void onDataNotAvailable(@NonNull BaseHttpError error);
}
