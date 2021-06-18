package com.aslan.baselibrary.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class VBBaseDialog<VB extends ViewBinding> extends BaseDialogFragment {

  protected VB mViewBinding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
    Class cls = (Class) type.getActualTypeArguments()[0];
    try {
      Method inflate = cls
          .getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
      mViewBinding = (VB) inflate.invoke(null, inflater, container, false);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return mViewBinding.getRoot();
  }

  @Override
  public final void iniView(@NonNull View view) {
    iniView();
  }

  public abstract void iniView();

  @Override
  public void onDestroy() {
    super.onDestroy();
    mViewBinding = null;
  }
}