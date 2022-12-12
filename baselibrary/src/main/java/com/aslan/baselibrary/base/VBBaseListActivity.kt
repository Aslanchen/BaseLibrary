package com.aslan.baselibrary.base

import android.os.SystemClock
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.Size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.R
import com.aslan.baselibrary.http.observer.DataObserver
import com.aslan.baselibrary.items.ProgressItem
import com.aslan.baselibrary.items.ProgressItem.StatusEnum
import com.aslan.baselibrary.utils.InflateActivity
import com.aslan.baselibrary.view.EmptyView
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

open abstract class VBBaseListActivity<M, VB : ViewBinding>(inflate: InflateActivity<VB>) :
    VBBaseActivity<VB>(inflate), FlexibleAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.EndlessScrollListener {

    enum class UpdateState {
        Refresh, LoadMore
    }

    protected lateinit var adapter: FlexibleAdapter<IFlexible<*>>
    protected var mProgressItem: IFlexible<*>? = null

    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected lateinit var recyclerView: RecyclerView
    protected var mEmptyView: EmptyView? = null

    @CallSuper
    override fun iniView() {
        initSwipeRefreshView()
        initRecyclerView()
        initApater()
        initEmptyView()
    }

    protected open fun getProgressItem(): IFlexible<*>? {
        if (mProgressItem == null) {
            mProgressItem = ProgressItem()
        }
        return mProgressItem
    }

    protected open fun initSwipeRefreshView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.isEnabled = true
    }

    protected open fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = SmoothScrollLinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.addItemDecoration(FlexibleItemDecoration(requireContext()))
    }

    protected open fun initApater() {
        adapter = FlexibleAdapter(null, this)
        val progressItem = getProgressItem()
        if (progressItem != null) {
            adapter.setEndlessScrollListener(this, progressItem)
                .setEndlessPageSize(getPageSize())
                .isTopEndless = false
        }
        adapter.mode = SelectableAdapter.Mode.IDLE
        recyclerView.adapter = adapter
    }

    protected open fun getEmptyLayoutResource() = -1
    protected open fun initEmptyView() {
        mEmptyView = findViewById(R.id.list_empty_view)
        if (mEmptyView != null) {
            if (getEmptyLayoutResource() != -1) {
                mEmptyView!!.setLayoutResource(getEmptyLayoutResource())
            }
            EmptyViewHelper.create(adapter, mEmptyView, null)
        }
    }

    @CallSuper
    override fun iniListener() {
        swipeRefreshLayout?.setOnRefreshListener(this)
    }

    override fun iniData() {
        lifecycleScope.launchWhenResumed {
            autoRefresh()
        }
    }

    protected fun autoRefresh() {
        swipeRefreshLayout?.isRefreshing = true
        onRefresh()
    }

    protected var DEFAULTINTERVAL = 1000
    private var mLastClickTime = 0L
    protected fun isSafeClick(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (now - mLastClickTime < DEFAULTINTERVAL) {
            return false
        }
        mLastClickTime = now
        return true
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        if (!isSafeClick()) {
            return false
        }

        val abstractFlexibleItem = adapter.getItem(position) ?: return false
        if (abstractFlexibleItem is ProgressItem) {
            onLoadMoreItemClick()
        } else if (view.id == -1) {
            onItemClick(abstractFlexibleItem, position)
        } else {
            onSubItemClick(abstractFlexibleItem, view, position)
        }
        return false
    }

    protected open fun onItemClick(item: IFlexible<*>, position: Int): Boolean {
        return false
    }

    protected open fun onSubItemClick(item: IFlexible<*>, view: View, position: Int): Boolean {
        return false
    }

    protected open fun onLoadMoreItemClick() {
        if (mProgressItem is ProgressItem) {
            if ((mProgressItem as ProgressItem).status == StatusEnum.ON_ERROR
            ) {
                onLoadMore(adapter.mainItemCount - 1, adapter.endlessCurrentPage)
            }
        }
    }

    open override fun onRefresh() {
        getDatas(UpdateState.Refresh, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false))
            .doFinally {
                swipeRefreshLayout?.setRefreshing(false)
            }
            .subscribe(object : DataObserver<List<M>>(this) {
                override fun handleSuccess(t: List<M>) {
                    addToListView(UpdateState.Refresh, t)
                }
            })
    }

    override fun noMoreLoad(newItemsSize: Int) {
    }

    open override fun onLoadMore(lastPosition: Int, currentPage: Int) {
        getDatas(UpdateState.LoadMore, currentPage + 1)
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false))
            .subscribe(object : DataObserver<List<M>>(this) {
                override fun handleSuccess(t: List<M>) {
                    addToListView(UpdateState.LoadMore, t)
                }
            })
    }

    protected abstract fun getItem(model: M): IFlexible<*>

    /**
     * @param curPage 当前页数，从1开始
     */
    protected abstract fun getDatas(
        rushState: UpdateState,
        @Size(min = 1) curPage: Int
    ): Observable<List<M>>

    protected open fun addToListView(rushState: UpdateState, datas: List<M>) {
        if (rushState == UpdateState.Refresh && datas.isEmpty()) {
            adapter.updateDataSet(null)
            adapter.onLoadMoreComplete(null)
            return
        }

        val items = ArrayList<IFlexible<*>>()
        for (model in datas) {
            val item = getItem(model)
            items.add(item)
        }

        if (rushState == UpdateState.Refresh) {
            adapter.updateDataSet(items)
            if (items.size < getPageSize()) {
                adapter.onLoadMoreComplete(null)
            } else {
                adapter.setEndlessProgressItem(getProgressItem())
            }
        } else if (rushState == UpdateState.LoadMore) {
            adapter.onLoadMoreComplete(items, -1)
        }
    }

    protected open fun getPageSize(): Int {
        return 20
    }
}