package com.aslan.baselibrary.base;

import android.arch.lifecycle.Observer;
import android.database.Observable;
import android.support.annotation.Nullable;

/**
 * 观察者模式
 */
public class DataObservable<T> extends Observable<Observer<T>> {

  public boolean hasRegisterObserver(Observer<T> observer) {
    synchronized (mObservers) {
      int index = mObservers.indexOf(observer);
      if (index == -1) {
        return false;
      }
      return true;
    }
  }

  public void notifyChanged(@Nullable T data) {
    synchronized (mObservers) {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        mObservers.get(i).onChanged(data);
      }
    }
  }

  public void registerObserver(@Nullable Observer<T> observer) {
    if (observer == null) {
      return;
    }

    synchronized (mObservers) {
      if (mObservers.contains(observer)) {
        return;
      }
    }
    super.registerObserver(observer);
  }

  @Override
  public void unregisterObserver(Observer<T> observer) {
    if (observer == null) {
      return;
    }

    synchronized (mObservers) {
      if (!mObservers.contains(observer)) {
        return;
      }
    }
    super.unregisterObserver(observer);
  }
}
