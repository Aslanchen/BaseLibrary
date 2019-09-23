package com.aslan.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.aslan.baselibrary.R;

/**
 * 列表
 *
 * @author Aslan
 * @date 2019/7/11
 */
public class ContentListFather extends BaseActivity {

  private static final String TAG_LAYOUT_ID = "layoutID";
  private static final String TAG_FRAGMENT = "fname";
  private static final String TAG_DATA = "args";

  private int layoutID = 0;
  private String fname;
  private Bundle args;

  public static Intent newIntent(Context context, Class<? extends Fragment> fClass) {
    Intent intent = new Intent(context, ContentListFather.class);
    intent.putExtra(TAG_FRAGMENT, fClass.getName());
    return intent;
  }

  public static Intent newIntent(Context context, Class<? extends Fragment> fClass, Bundle args) {
    Intent intent = new Intent(context, ContentListFather.class);
    intent.putExtra(TAG_FRAGMENT, fClass.getName());
    intent.putExtra(TAG_DATA, args);
    return intent;
  }

  public static Intent newIntent(Context context, int layoutID, Class<? extends Fragment> fClass) {
    Intent intent = new Intent(context, ContentListFather.class);
    intent.putExtra(TAG_LAYOUT_ID, layoutID);
    intent.putExtra(TAG_FRAGMENT, fClass.getName());
    return intent;
  }

  public static Intent newIntent(Context context, int layoutID, Class<? extends Fragment> fClass,
      Bundle args) {
    Intent intent = new Intent(context, ContentListFather.class);
    intent.putExtra(TAG_LAYOUT_ID, layoutID);
    intent.putExtra(TAG_FRAGMENT, fClass.getName());
    intent.putExtra(TAG_DATA, args);
    return intent;
  }

  @Override
  public void iniBundle(@NonNull Bundle bundle) {
    layoutID = bundle.getInt(TAG_LAYOUT_ID, R.layout.content_list_father);
    fname = bundle.getString(TAG_FRAGMENT);
    args = bundle.getBundle(TAG_DATA);
  }

  @Override
  public int getLayoutId() {
    return layoutID;
  }

  @Override
  public void iniView() {

  }

  @Override
  public void iniListener() {

  }

  @Override
  public void iniData() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.flList, Fragment.instantiate(this, fname, args));
    fragmentTransaction.commit();
  }
}
