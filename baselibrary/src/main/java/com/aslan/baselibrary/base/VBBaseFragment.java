package com.aslan.baselibrary.base;

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
public abstract class VBBaseFragment<VB extends ViewBinding> extends BaseFragment {

  protected VB mViewBinding;

  @Override
  public View setCusContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
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
  public final void iniView(View view) {
    iniView();
  }

  public abstract void iniView();

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mViewBinding = null;
  }
}