package com.aslan.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aslan.baselibrary.listener.IBaseView;
import com.aslan.baselibrary.listener.IMVPBasePresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.Factory;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class MVPBasePresenter<V extends IBaseView> implements IMVPBasePresenter {

    public V mView;
    public Context context;
    public LifecycleOwner lifecycleOwner;
    public BaseActivity activity;
    public BaseFragment fragment;

    @SuppressWarnings({"unchecked"})
    public MVPBasePresenter(BaseActivity activity) {
        this.context = activity;
        this.lifecycleOwner = activity;
        this.mView = (V) activity;
        this.activity = activity;
    }

    @SuppressWarnings({"unchecked"})
    public MVPBasePresenter(BaseFragment fragment) {
        this.context = fragment.getContext();
        this.lifecycleOwner = fragment;
        this.mView = (V) fragment;
        this.fragment = fragment;
        this.activity = (BaseActivity) fragment.getActivity();
    }

    @Override
    public void iniBundle(@NonNull Bundle bundle) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @Override
    public void onCreate() {
    }

    @Override
    public void iniData() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @Override
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @Override
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @Override
    public void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    public void onDestroy() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (this instanceof PermissionCallbacks) {
            EasyPermissions
                    .onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    @Nullable
    @Override
    public FragmentManager getFragmentManager() {
        if (fragment != null) {
            return fragment.getFragmentManager();
        }

        return activity.getSupportFragmentManager();
    }

    /**
     * EasyPermissions 使用
     */
    protected boolean somePermissionDenied(@NonNull String... perms) {
        if (fragment == null) {
            return EasyPermissions.somePermissionDenied(activity, perms);
        } else {
            return EasyPermissions.somePermissionDenied(fragment, perms);
        }
    }

    /**
     * EasyPermissions 使用
     */
    protected void requestPermissions(@StringRes int rationaleResId, int requestCode,
                                      @Size(min = 1) @NonNull String... perms) {
        requestPermissions(context.getString(rationaleResId), requestCode, perms);
    }

    /**
     * EasyPermissions 使用
     */
    protected void requestPermissions(@NonNull String rationale, int requestCode,
                                      @Size(min = 1) @NonNull String... perms) {
        if (fragment == null) {
            EasyPermissions.requestPermissions(activity, rationale, requestCode, perms);
        } else {
            EasyPermissions.requestPermissions(fragment, rationale, requestCode, perms);
        }
    }

    /**
     * EasyPermissions 使用
     */
    protected PermissionRequest.Builder newPermissionRequestBuilder(int requestCode,
                                                                    @NonNull @Size(min = 1)
                                                                            String... perms) {
        if (fragment == null) {
            return new PermissionRequest.Builder(activity, requestCode, perms);
        } else {
            return new PermissionRequest.Builder(fragment, requestCode, perms);
        }
    }

    /**
     * EasyPermissions 使用,进入设置界面
     */
    protected AppSettingsDialog.Builder newAppSettingsDialogBuilder() {
        if (fragment == null) {
            return new AppSettingsDialog.Builder(activity);
        } else {
            return new AppSettingsDialog.Builder(fragment);
        }
    }

    /**
     * ViewModelProviders.of 使用
     */
    protected ViewModelProvider ViewModelProvidersOf() {
        if (fragment != null) {
            return new ViewModelProvider(fragment);
        } else {
            return new ViewModelProvider(activity);
        }
    }

    /**
     * ViewModelProviders.of 使用
     */
    protected ViewModelProvider ViewModelProvidersOf(@Nullable Factory factory) {
        if (fragment != null) {
            return new ViewModelProvider(fragment, factory);
        } else {
            return new ViewModelProvider(activity, factory);
        }
    }
}