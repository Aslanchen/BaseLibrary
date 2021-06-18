package com.aslan.baselibrary.listener;

import androidx.annotation.NonNull;
import com.aslan.baselibrary.base.DataError;
import java.util.List;

public interface LoadListCallback<T> {

  void onLoaded(@NonNull List<T> responses);

  void onDataNotAvailable(@NonNull DataError error);
}
