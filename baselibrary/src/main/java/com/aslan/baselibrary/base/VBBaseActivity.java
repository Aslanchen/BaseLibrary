package com.aslan.baselibrary.base;

import android.view.LayoutInflater;
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
public abstract class VBBaseActivity<VB extends ViewBinding> extends BaseActivity {

  protected VB mViewBinding;

  @Override
  public void setCusContentView() {
    ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
    Class cls = (Class) type.getActualTypeArguments()[0];
    try {
      Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
      mViewBinding = (VB) inflate.invoke(null, getLayoutInflater());
      setContentView(mViewBinding.getRoot());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    setContentView(mViewBinding.getRoot());
  }
}