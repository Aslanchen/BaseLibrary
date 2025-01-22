package com.aslan.app.views

import android.os.Bundle
import com.aslan.app.base.ActivityBaseListSimple
import com.aslan.app.databinding.ActivityMenusBinding
import com.aslan.app.views.items.ItemMenu
import com.aslan.baselibrary.base.UpdateState
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable

class HomeActivity : ActivityBaseListSimple<HomeActivity.Menu, ActivityMenusBinding>(ActivityMenusBinding::inflate) {

    override fun iniBundle(bundle: Bundle) {}

    override fun getItem(model: Menu) = ItemMenu(model)

    override fun getDatas(rushState: UpdateState, curPage: Int): Observable<List<Menu>> {
        val list = mutableListOf<Menu>()
        list.add(Menu("输入界面") { startActivity(InputActivity.newIntent(requireContext())) })
        list.add(Menu("权限界面") { startActivity(PermissionActivity.newIntent(requireContext())) })
        return Observable.just(list)
    }

    override fun onItemClick(item: IFlexible<*>, position: Int): Boolean {
        if (item is ItemMenu) {
            item.data.action()
        }
        return super.onItemClick(item, position)
    }

    inner class Menu(val v: String, val action: () -> Unit)
}