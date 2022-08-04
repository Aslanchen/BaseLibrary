package com.aslan.baselibrary.base

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.Size
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.R
import com.aslan.baselibrary.http.observer.DataObserver
import com.aslan.baselibrary.items.ProgressItem
import com.aslan.baselibrary.utils.InflateFragment
import com.aslan.baselibrary.view.EmptyView
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class VBBaseListFragment<M, VB : ViewBinding>(inflate: InflateFragment<VB>) :
    VBBaseFragment<VB>(inflate), FlexibleAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.EndlessScrollListener {

    protected lateinit var adapter: FlexibleAdapter<IFlexible<*>>

    protected var progressItem = ProgressItem()

    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var listEmptyView: EmptyView

    @CallSuper
    override fun iniView(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.recycler_view)
        listEmptyView = view.findViewById(R.id.list_empty_view)

        swipeRefreshLayout?.isEnabled = true

        recyclerView.layoutManager = SmoothScrollLinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        addItemDecoration(recyclerView)

        adapter = FlexibleAdapter(null, this)
        adapter.setEndlessScrollListener(this, progressItem)
            .setEndlessPageSize(getPageSize())
            .isTopEndless = false
        adapter.mode = SelectableAdapter.Mode.IDLE

        recyclerView.adapter = adapter
    }

    protected open fun addItemDecoration(recyclerView: RecyclerView) {
    }

    @CallSuper
    override fun iniListener() {
        swipeRefreshLayout?.setOnRefreshListener(this)
    }

    override fun iniData() {
        autoRefresh()
    }

    protected fun autoRefresh() {
        swipeRefreshLayout?.isRefreshing = true
        onRefresh()
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        val abstractFlexibleItem = adapter.getItem(position) ?: return false
        if (abstractFlexibleItem is ProgressItem) {
            onLoadMoreItemClick()
        } else if (view.id == -1) {
            return onItemClick(abstractFlexibleItem, position)
        } else {
            return onSubItemClick(abstractFlexibleItem, view, position)
        }
        return false
    }

    protected open fun onItemClick(abstractFlexibleItem: IFlexible<*>, position: Int): Boolean {
        return false
    }

    protected open fun onSubItemClick(
        abstractFlexibleItem: IFlexible<*>,
        view: View,
        position: Int
    ): Boolean {
        return false
    }

    protected fun onLoadMoreItemClick() {
        if (progressItem.status == ProgressItem.StatusEnum.ON_ERROR) {
            onLoadMore(adapter.mainItemCount - 1, adapter.endlessCurrentPage)
        }
    }

    open override fun onRefresh() {
        adapter.setEndlessProgressItem(progressItem)
        getDataFromNet(VBBaseListActivity.UpdateState.Refresh, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false))
            .doFinally {
                swipeRefreshLayout?.setRefreshing(false)
            }
            .subscribe(object : DataObserver<List<M>>(requireContext()) {
                override fun handleSuccess(t: List<M>) {
                    if (t.isEmpty()) {
                        adapter.setEndlessProgressItem(null)
                    } else {
                        adapter.setEndlessProgressItem(progressItem)
                    }
                    addToListView(VBBaseListActivity.UpdateState.Refresh, t)
                }
            })
    }

    override fun noMoreLoad(newItemsSize: Int) {
        progressItem.status = ProgressItem.StatusEnum.NO_MORE_LOAD
    }

    open override fun onLoadMore(lastPosition: Int, currentPage: Int) {
        progressItem.status = ProgressItem.StatusEnum.MORE_TO_LOAD
        getDataFromNet(
            VBBaseListActivity.UpdateState.LoadMore,
            currentPage + 1,
        )
            .observeOn(AndroidSchedulers.mainThread())
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false))
            .bindToLifecycle(this)
            .doOnError {
                progressItem.status = ProgressItem.StatusEnum.ON_ERROR
            }
            .subscribe(object : DataObserver<List<M>>(requireContext()) {
                override fun handleSuccess(t: List<M>) {
                    addToListView(VBBaseListActivity.UpdateState.LoadMore, t)
                }
            })
    }

    protected abstract fun getItem(model: M): IFlexible<*>

    /**
     * @param curPage 当前页数，从1开始
     */
    protected abstract fun getDataFromNet(
        rushState: VBBaseListActivity.UpdateState,
        @Size(min = 1) curPage: Int
    ): Observable<List<M>>

    protected open fun addToListView(rushState: VBBaseListActivity.UpdateState, datas: List<M>) {
        val items = ArrayList<IFlexible<*>>()
        for (model in datas) {
            val item = getItem(model)
            items.add(item)
        }

        if (rushState == VBBaseListActivity.UpdateState.Refresh) {
            adapter.updateDataSet(items)
        } else if (rushState == VBBaseListActivity.UpdateState.LoadMore) {
            adapter.onLoadMoreComplete(items, -1)
        }
    }

    open fun getPageSize(): Int {
        return 20
    }
}