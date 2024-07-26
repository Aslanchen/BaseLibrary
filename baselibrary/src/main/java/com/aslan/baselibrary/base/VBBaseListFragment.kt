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
import com.aslan.baselibrary.http.observer.DataMaybeObserver
import com.aslan.baselibrary.items.ProgressItem
import com.aslan.baselibrary.listener.SafeClickListener
import com.aslan.baselibrary.utils.InflateFragment
import com.aslan.baselibrary.view.EmptyView
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.SelectableAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 对[RecyclerView]进行了封装。
 *
 * @author Aslan
 * @date 2023/04/06
 */
abstract class VBBaseListFragment<M, A : FlexibleAdapter<IFlexible<*>>, VB : ViewBinding>(
    inflate: InflateFragment<VB>
) :
    VBBaseFragment<VB>(inflate), FlexibleAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.EndlessScrollListener {

    protected lateinit var adapter: A
    protected var mProgressItem: IFlexible<*>? = null

    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected lateinit var recyclerView: RecyclerView
    protected var mEmptyView: EmptyView? = null

    @CallSuper
    override fun iniView(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.recycler_view)
        mEmptyView = view.findViewById(R.id.list_empty_view)

        initSwipeRefreshView(view)
        initRecyclerView(view)
        initAdapter()
        initEmptyView(view)
    }

    /**
     * 对加载更多进行了封装，可以通过[ProgressItem]进行自定义，返回空则不显示加载更多。
     */
    protected open fun getProgressItem(): IFlexible<*>? {
        if (mProgressItem == null) {
            mProgressItem = ProgressItem()
        }
        return mProgressItem
    }

    protected open fun initSwipeRefreshView(view: View) {
        swipeRefreshLayout?.isEnabled = true
    }

    protected open fun initRecyclerView(view: View) {
        recyclerView.layoutManager = SmoothScrollLinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.addItemDecoration(FlexibleItemDecoration(requireContext()))
    }

    protected abstract fun instanceAdapter(): A
    protected open fun initAdapter() {
        adapter = instanceAdapter()
        if (isPaging()) {
            val progressItem = getProgressItem()
            if (progressItem != null) {
                adapter.setEndlessScrollListener(this, progressItem)
                    .setEndlessPageSize(getPageSize())
                    .isTopEndless = false
            }
        }
        adapter.mode = getAdapterMode()
        recyclerView.adapter = adapter
    }

    /**
     * 自定义空页面
     */
    protected open fun getEmptyLayoutResource() = -1

    /**
     * 是否分页
     */
    protected open fun isPaging() = true

    protected open fun getAdapterMode() = SelectableAdapter.Mode.IDLE

    /**
     * 对空页面进行了封装，可以通过[EmptyView]进行自定义。
     */
    protected open fun initEmptyView(view: View) {
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

    protected open var isRefreshing = false
    protected fun autoRefresh() {
        if (isRefreshing) {
            return
        }

        swipeRefreshLayout?.isRefreshing = true
        isRefreshing = true
        onRefresh()
    }

    open protected var DEFAULTINTERVAL = SafeClickListener.DEFAUL_TINTERVAL
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
            return onItemClick(abstractFlexibleItem, position)
        } else {
            return onSubItemClick(abstractFlexibleItem, view, position)
        }
        return false
    }

    protected open fun onItemClick(item: IFlexible<*>, position: Int): Boolean {
        return false
    }

    protected open fun onSubItemClick(item: IFlexible<*>, view: View, position: Int): Boolean {
        return false
    }

    protected fun onLoadMoreItemClick() {
        if (mProgressItem is ProgressItem) {
            if ((mProgressItem as ProgressItem).status == ProgressItem.StatusEnum.ON_ERROR
            ) {
                onLoadMore(adapter.mainItemCount - 1, adapter.endlessCurrentPage)
            }
        }
    }

    open override fun onRefresh() {
        getDatas(UpdateState.Refresh, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false, isShowToast = isShowToast()))
            .doFinally {
                swipeRefreshLayout?.isRefreshing = false
                isRefreshing = false
            }
            .subscribe(object : DataMaybeObserver<List<M>>(requireContext()) {
                override fun handleSuccess(t: List<M>?) {
                    if (isPaging()) {
                        if (getProgressItem() is ProgressItem) {
                            (getProgressItem() as ProgressItem).status = ProgressItem.StatusEnum.MORE_TO_LOAD
                            val index = adapter.getGlobalPositionOf(getProgressItem())
                            if (index >= 0) {
                                adapter.notifyItemChanged(index)
                            }
                        }
                        adapter.setEndlessProgressItem(getProgressItem())
                    }

                    addToListView(UpdateState.Refresh, t)
                }
            })
    }

    override fun noMoreLoad(newItemsSize: Int) {
    }

    open fun isShowToast(): Boolean {
        return true
    }

    open override fun onLoadMore(lastPosition: Int, currentPage: Int) {
        getDatas(UpdateState.LoadMore, currentPage + 1)
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(this)
            .compose(DataTransformer(mBaseView = this, isShowProgressbar = false, isShowToast = isShowToast()))
            .subscribe(object : DataMaybeObserver<List<M>>(requireContext()) {
                override fun handleSuccess(t: List<M>?) {
                    addToListView(UpdateState.LoadMore, t)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    if (isPaging()) {
                        if (getProgressItem() is ProgressItem) {
                            (getProgressItem() as ProgressItem).status = ProgressItem.StatusEnum.ON_ERROR
                            val index = adapter.getGlobalPositionOf(getProgressItem())
                            if (index >= 0) {
                                adapter.notifyItemChanged(index)
                            }
                        }
                    }
                    adapter.onLoadMoreComplete(null, -1)
                }
            })
    }

    protected abstract fun getItem(model: M): IFlexible<*>

    /**
     * @param curPage 当前页数，从1开始
     */
    protected abstract fun getDatas(rushState: UpdateState, @Size(min = 1) curPage: Int): Maybe<List<M>>

    protected open fun addToListView(rushState: UpdateState, datas: List<M>?) {
        if (rushState == UpdateState.Refresh && datas.isNullOrEmpty()) {
            adapter.updateDataSet(null)
            adapter.onLoadMoreComplete(null)
            return
        }

        val items = ArrayList<IFlexible<*>>()
        for (model in datas!!) {
            val item = getItem(model)
            items.add(item)
        }

        if (rushState == UpdateState.Refresh) {
            adapter.updateDataSet(items)
            if (isPaging()) {
                if (items.size < getPageSize()) {
                    adapter.onLoadMoreComplete(null)
                }
            }
        } else if (rushState == UpdateState.LoadMore) {
            adapter.onLoadMoreComplete(items, -1)
        }
    }

    open fun getPageSize(): Int {
        return 20
    }
}