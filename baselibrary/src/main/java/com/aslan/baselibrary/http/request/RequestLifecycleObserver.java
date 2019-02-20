package com.aslan.baselibrary.http.request;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * 接口和生命周期做绑定
 *
 * @author Aslan
 * @date 2019/2/19
 */
public class RequestLifecycleObserver implements LifecycleObserver {

  private RequestCall requestCall;

  public RequestLifecycleObserver(RequestCall requestCall) {
    this.requestCall = requestCall;
  }

  @OnLifecycleEvent(Event.ON_DESTROY)
  public void onDestroy() {
    if (requestCall != null) {
      requestCall.cancel();
    }
  }
}
