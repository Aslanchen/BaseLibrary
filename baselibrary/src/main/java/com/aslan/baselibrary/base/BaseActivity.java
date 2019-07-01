package com.aslan.baselibrary.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.IBaseView;
import com.aslan.baselibrary.view.CustomToolbar;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

  protected ProgressDialog progressDialog;
  protected CustomToolbar titleBar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (savedInstanceState != null) {
      intent.putExtras(savedInstanceState);
    }

    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      iniBundle(bundle);
    }

    setCusContentView();

    titleBar = findViewById(R.id.titleBar);
    if (titleBar != null) {
      titleBar.setNavigationOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          navigationOnClickListener();
        }
      });
    }

    iniView();
    iniListener();
    iniData();
  }

  public void setCusContentView() {
    setContentView(getLayoutId());
  }

  public abstract void iniBundle(@NonNull Bundle bundle);

  public abstract int getLayoutId();

  public abstract void iniView();

  public abstract void iniListener();

  public abstract void iniData();

  @Override
  public void setTitle(CharSequence title) {
    titleBar.setTitle(title);
  }

  @Override
  public void setTitle(@StringRes int titleId) {
    titleBar.setTitle(titleId);
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
      progressDialog = new ProgressDialog(this);
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

    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull CharSequence text) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @UiThread
  @Override
  public void showToastMessage(@NonNull BaseHttpError error) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(this, error.getMsg(), Toast.LENGTH_SHORT).show();
  }

  public void navigationOnClickListener() {
    thisFinish();
  }

  @MainThread
  @Override
  public void thisFinish() {
    this.finish();
  }

  @Override
  public boolean isAdd() {
    return !isFinishing();
  }

  @Override
  protected void onDestroy() {
    closeProgressBar();
    super.onDestroy();
  }
}