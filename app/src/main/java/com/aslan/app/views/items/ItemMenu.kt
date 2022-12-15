package com.aslan.app.views.items

import android.view.View
import com.aslan.app.views.ListActivity
import com.aslan.app.R
import com.aslan.app.databinding.ItemMenuBinding
import com.aslan.baselibrary.base.VBBaseViewHolder
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class ItemMenu(val data: ListActivity.Menu) : AbstractFlexibleItem<ItemMenu.ViewHolder>() {

    override fun equals(o: Any?): Boolean {
        if (o is ItemMenu) {
            return data.equals(o.data);
        }
        return this == o
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_menu
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        val context = holder.itemView.context
        holder.mViewBinding.tv.setText(data.v)
    }

    inner class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        VBBaseViewHolder<ItemMenuBinding>(view, adapter, ItemMenuBinding::bind) {
        init {

        }

        override fun toggleActivation() {
            super.toggleActivation()
        }
    }
}