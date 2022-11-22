package com.aslan.app

import android.os.Bundle
import com.aslan.app.databinding.ActivityMenusBinding
import com.aslan.app.items.ItemMenu
import com.aslan.baselibrary.base.VBBaseListActivity
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable

class ListActivity :
    VBBaseListActivity<ListActivity.Menu, ActivityMenusBinding>(ActivityMenusBinding::inflate) {

    override fun iniBundle(bundle: Bundle) {
    }

    override fun getItem(model: Menu) = ItemMenu(model)

    override fun getDatas(rushState: UpdateState, curPage: Int): Observable<List<Menu>> {
        val list = mutableListOf<Menu>()
        if (rushState == UpdateState.Refresh) {
            for (i in 0 until 22) {
                list.add(Menu(i.toString()))
            }
        } else {
            for (i in 0 until 5) {
                list.add(Menu(i.toString()))
            }
        }
        return Observable.fromArray(list)
    }

    override fun onItemClick(item: IFlexible<*>, position: Int): Boolean {
        if (item is ItemMenu) {
            if (position == 0) {

            }
        }
        return super.onItemClick(item, position)
    }

    inner class Menu(val v: String)
}