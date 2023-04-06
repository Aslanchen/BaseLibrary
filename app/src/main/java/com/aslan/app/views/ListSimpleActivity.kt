package com.aslan.app.views

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aslan.app.databinding.ActivityMenusBinding
import com.aslan.app.views.items.ItemMenu
import com.aslan.baselibrary.base.UpdateState
import com.aslan.baselibrary.base.VBBaseListSimpleActivity
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import kotlinx.coroutines.delay

class ListSimpleActivity :
    VBBaseListSimpleActivity<ListSimpleActivity.Menu, ActivityMenusBinding>(ActivityMenusBinding::inflate) {

    override fun iniBundle(bundle: Bundle) {}

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

    override fun iniData() {
        super.iniData()

        lifecycleScope.launchWhenResumed {
            showProgressBar("AAAAAAA")
            delay(1000L)
            closeProgressBar()

            delay(3000L)

            showProgressBar("BBBBBBB")
            delay(1000L)
            closeProgressBar()
        }
    }

    inner class Menu(val v: String)
}