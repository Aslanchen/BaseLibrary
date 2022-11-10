package com.aslan.baselibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aslan.baselibrary.R;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

/**
 * 空页面 Aslanchen
 */
public class EmptyView extends LinearLayout {

    private ImageView iv_empty;
    private TextView tv_empty;
    private ViewStub viewStub;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, this, true);
        iv_empty = view.findViewById(R.id.iv_empty);
        tv_empty = view.findViewById(R.id.tv_empty);
        viewStub = view.findViewById(R.id.vs);
        setVisibility(View.GONE);
    }

    public View initViewStub(@LayoutRes int id) {
        iv_empty.setVisibility(View.GONE);
        tv_empty.setVisibility(View.GONE);

        viewStub.setLayoutResource(id);
        return viewStub.inflate();
    }

    public void setEmptyImageResource(@DrawableRes int resId) {
        iv_empty.setImageResource(resId);
    }

    public void setEmptyImageVisibility(int visibility) {
        iv_empty.setVisibility(visibility);
    }

    public void setEmptyTextVisibility(int visibility) {
        tv_empty.setVisibility(visibility);
    }

    public void setEmptyText(CharSequence text) {
        tv_empty.setText(text);
    }

    public void setEmptyText(@StringRes int resid) {
        tv_empty.setText(resid);
    }

    public void setEmptyTextColor(@ColorInt int color) {
        tv_empty.setTextColor(color);
    }
}
