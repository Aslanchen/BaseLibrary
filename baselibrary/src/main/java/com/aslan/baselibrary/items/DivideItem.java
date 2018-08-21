package com.aslan.baselibrary.items;

import android.view.View;
import com.aslan.baselibrary.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * 中间灰色空格 Created by xjx on 2017/9/22.
 */
public class DivideItem extends AbstractFlexibleItem<DivideItem.ItemHolder> {

  private float defaultHeight = 0;

  public DivideItem() {
  }

  public DivideItem(float defaultHeight) {
    this.defaultHeight = defaultHeight;
  }

  @Override
  public boolean equals(Object o) {
    return false;
  }

  @Override
  public int getLayoutRes() {
    return R.layout.item_divide;
  }

  @Override
  public ItemHolder createViewHolder(View view, FlexibleAdapter adapter) {
    return new ItemHolder(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ItemHolder holder, int position,
      List payloads) {
    if (defaultHeight > 0) {
      holder.v_divider.getLayoutParams().height = (int) defaultHeight;
    }
  }

  class ItemHolder extends FlexibleViewHolder {

    private View v_divider;

    public ItemHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      v_divider = view.findViewById(R.id.v_divider);
    }
  }
}
