package com.aslan.baselibrary.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aslan.baselibrary.http.BaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

/**
 * 错误提示类以及等待框
 *
 * @author Aslan
 * @date 2018/4/11
 */
@UiThread
public interface IBaseView {

    @Nullable
    Context getContext();

    void showProgressBar();

    void showProgressBar(@StringRes int msg);

    void showProgressBar(@NonNull String msg);

    void showProgressBar(boolean canCancel);

    void showProgressBar(boolean canCancel, @StringRes int msg);

    void showProgressBar(boolean canCancel, @NonNull String msg);

    void closeProgressBar();

    void showToastMessage(@StringRes int resId);

    void showToastMessage(@NonNull CharSequence text);

    void showToastMessage(@NonNull BaseError error);

    boolean isAdd();

    void startActivity(Intent intent);

    void startActivityForResult(@RequiresPermission Intent intent, int requestCode);

    void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
                                @Nullable Bundle options);

    void thisFinish();
}