package com.aslan.baselibrary.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseError;
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
  public void showToastMessage(@NonNull BaseError error) {
    if (isAdd() == false) {
      return;
    }

    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (this instanceof PermissionCallbacks) {
      EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
  }
}