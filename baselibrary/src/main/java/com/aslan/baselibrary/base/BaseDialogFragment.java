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
    String message = getString(msg);
    showProgressBar(canCancel, message);
  }

  @UiThread
  @Override
  public void showProgressBar(boolean canCancel, @NonNull String msg) {
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
    if (progressDialog != null) {
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
    Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull CharSequence text) {
    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull BaseHttpError error) {
    Toast.makeText(getContext(), error.getMsg(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean isAdd() {
    return this.isAdded();
  }
}