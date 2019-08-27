package com.aslan.baselibrary.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.IBaseView;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class BaseDialogFragment extends DialogFragment implements IBaseView {

  protected ProgressDialog progressDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getArguments();
    if (bundle != null) {
      iniBundle(bundle);
    }
  }

  public abstract void iniBundle(@NonNull Bundle bundle);

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