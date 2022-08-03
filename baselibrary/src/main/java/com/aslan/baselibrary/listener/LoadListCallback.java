package com.aslan.baselibrary.listener;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * 数据加载回调
 *
 * @param <T>
 */
public interface LoadListCallback<T> {

  void onLoaded(@NonNull List<T> responses);

  void onDataNotAvailable(@NonNull Throwable error);
}
