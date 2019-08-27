package com.aslan.baselibrary.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.IBaseView;
import com.aslan.baselibrary.view.CustomToolbar;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class BaseFragment extends Fragment implements IBaseView {

  protected ProgressDialog progressDialog;
  protected CustomToolbar titleBar;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getArguments();
    if (bundle != null) {
      iniBundle(bundle);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = setCusContentView(inflater, container);

    titleBar = view.findViewById(R.id.titleBar);
    if (titleBar != null) {
      titleBar.setNavigationOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          navigationOnClickListener();
        }
      });
    }
    return view;
  }

  public abstract void iniBundle(@NonNull Bundle bundle);

  public View setCusContentView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container) {
    return LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    iniView(view);
    iniListener();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    iniData();
  }

  public abstract int getLayoutId();

  public abstract void iniView(View view);

  public abstract void iniListener();

  public abstract void iniData();

  public void setTitle(@StringRes int resid) {
    titleBar.setTitle(resid);
  }

  public void setTitle(CharSequence text) {
    titleBar.setTitle(text);
  }

  @UiThread
  @Override
  public void showProgressBar() {
    showProgressBar(true);
  }

  @UiThread
  @Override
  public void showProgressBar(@NonNull String msg) {
    showProgressBar(true, msg);
  }

  @UiThread
  @Override
  public void showProgressBar(@StringRes int msg) {
    if (isAdd() == false) {
      return;
    }

    String message = getString(msg);
    showProgressBar(message);
  }

  @UiThread
  @Override
  public void showProgressBar(boolean canCancel) {
    showProgressBar(canCancel, R.string.progress_waiting);
  }

  @UiThread
  @Override
  public void showProgressBar(boolean canCancel, @StringRes int msg) {
    if (isAdd() == false) {
      return;
    }

    String message = getString(msg);
    showProgressBar(canCancel, message);
  }

  @UiThread
  @Override
  public void showProgressBar(boolean canCancel, @NonNull String msg) {
    if (isAdd() == false) {
      return;
    }

    if (progressDialog == null) {
      progressDialog = new ProgressDialog(getContext());
    }

    if (progressDialog.isShowing()) {
      return;
    }

    progressDialog.setMessage(msg);
    progressDialog.setCancelable(canCancel);
    progressDialog.setCanceledOnTouchOutside(canCancel);
    try {
      progressDialog.show();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @UiThread
  @Override
  public void closeProgressBar() {
    if (isAdd() == false) {
      return;
    }

    if (progressDialog != null && progressDialog.isShowing()) {
      try {
        progressDialog.dismiss();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  @UiThread
  @Override
  public void showToastMessage(@StringRes int resId) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull CharSequence text) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull BaseHttpError error) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(getContext(), error.getMsg(), Toast.LENGTH_SHORT).show();
  }

  public void navigationOnClickListener() {
    ((BaseActivity) getActivity()).thisFinish();
  }

  @MainThread
  @Override
  public void thisFinish() {
    Activity activity = getActivity();
    if (activity != null && activity instanceof BaseActivity) {
      ((BaseActivity) activity).thisFinish();
    }
  }

  @Override
  public boolean isAdd() {
    return this.isAdded();
  }

  @Override
  public void onDestroy() {
    closeProgressBar();
    super.onDestroy();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (this instanceof PermissionCallbacks) {
      EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
  }
}