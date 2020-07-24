package com.aslan.baselibrary.base;

import android.os.Handler;
import android.view.View;

import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseError;
import com.aslan.baselibrary.items.ProgressItem;
import com.aslan.baselibrary.listener.LoadListCallback;
import com.aslan.baselibrary.view.EmptyView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.helpers.ActionModeHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * 列表界面统一封装
 *
 * @author Aslanchen
 * @date 017/4/27.
 */
public abstract class BaseRecyleListFragment<M> extends BaseFragment implements
        FlexibleAdapter.OnUpdateListener, FlexibleAdapter.EndlessScrollListener,
        FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    public enum UpdateState {
        Refresh,
        LoadMore
    }

    protected FlexibleAdapter<AbstractFlexibleItem> adapter;
    protected ProgressItem progressItem = new ProgressItem();

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected EmptyView listEmptyView;
    protected ActionModeHelper mActionModeHelper;

    protected LoadListCallback<M> callbackRefresh;
    protected LoadListCallback<M> callbackLoad;
    protected int currentPage = 1;

    protected Handler mHandler = new Handler();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycler_view;
    }

    @CallSuper
    @Override
    public void iniView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recycler_view);
        listEmptyView = view.findViewById(R.id.list_empty_view);

        iniEmptyView();

        swipeRefreshLayout.setColorSchemeResources(getColorSchemeResources());
        swipeRefreshLayout.setEnabled(true);

        recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new FlexibleAdapter<AbstractFlexibleItem>(new ArrayList<AbstractFlexibleItem>(),
                this);
        adapter.setEndlessScrollListener(this, null);
        adapter.setEndlessPageSize(getDataCountPrePage());
        iniHead();
        recyclerView.setAdapter(adapter);

        mActionModeHelper = new ActionModeHelper(adapter, 0);
        mActionModeHelper.withDefaultMode(SelectableAdapter.Mode.IDLE);
        adapter.setMode(SelectableAdapter.Mode.IDLE);
    }

    @CallSuper
    @Override
    public void iniListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @CallSuper
    @Override
    public void iniData() {
        callbackRefresh = new LoadListCallback<M>() {
            @Override
            public void onLoaded(@NonNull List<M> respones) {
                addToListView(UpdateState.Refresh, respones);
            }

            @Override
            public void onDataNotAvailable(@NonNull BaseError error) {
                showToastMessage(error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        callbackLoad = new LoadListCallback<M>() {
            @Override
            public void onLoaded(@NonNull List<M> respones) {
                addToListView(UpdateState.LoadMore, respones);
            }

            @Override
            public void onDataNotAvailable(@NonNull BaseError error) {
                showToastMessage(error.getMessage());
                loadFialed();
            }
        };

        getDataFromCache(UpdateState.Refresh, 1, new LoadListCallback<M>() {
            @Override
            public void onLoaded(@NonNull List<M> respones) {
                addToListView(UpdateState.Refresh, respones);
                autoRefreshDelay();
            }

            @Override
            public void onDataNotAvailable(@NonNull BaseError error) {
                autoRefreshDelay();
            }
        });
    }

    protected void autoRefreshDelay() {
        mHandler.postDelayed(this::autoRefresh, 500L);
    }

    @UiThread
    protected void autoRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @CallSuper
    @Override
    public void onRefresh() {
        currentPage = 1;
        getDataFromNet(UpdateState.Refresh, currentPage, callbackRefresh);
    }

    @Override
    public void noMoreLoad(int newItemsSize) {

    }

    @CallSuper
    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        this.currentPage = currentPage + 1;
        getDataFromNet(UpdateState.LoadMore, this.currentPage, callbackLoad);
    }

    private void loadFialed() {
        progressItem.setStatus(ProgressItem.StatusEnum.ON_ERROR);
    }

    /**
     * item点击，需要注意的是，最外层view不能设置id，因为需要使用view.getId() == -1来做判断,可以使用itemView来替代使用进行样式或者值得设置
     *
     * @param view     the view that generated the event
     * @param position the adapter position of the item clicked
     */
    @Override
    public boolean onItemClick(View view, int position) {
        AbstractFlexibleItem abstractFlexibleItem = adapter.getItem(position);
        if (abstractFlexibleItem == null) {
            return false;
        }

        if (abstractFlexibleItem instanceof ProgressItem) {
            onLoadMoreItemClick();
            return false;
        } else if (view.getId() == -1) {
            onItemClick(abstractFlexibleItem, position);
        } else {
            onSubItemClick(abstractFlexibleItem, view, position);
        }
        return false;
    }

    public void onItemClick(AbstractFlexibleItem abstractFlexibleItem, int position) {

    }

    public void onSubItemClick(AbstractFlexibleItem abstractFlexibleItem, View view, int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onUpdateEmptyView(int size) {
        if (size > 0) {
            listEmptyView.setVisibility(View.GONE);
        } else {
            listEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void iniHead() {

    }

    public void beforeAddItem(List<M> list) {

    }

    protected void addToListView(UpdateState rushState, @NonNull List<M> list) {
        List<AbstractFlexibleItem> items = new ArrayList<>();

        beforeAddItem(list);

        for (M model : list) {
            AbstractFlexibleItem item = getItem(model);
            if (item == null) {
                continue;
            }
            items.add(item);
        }

        if (rushState == UpdateState.Refresh) {
            adapter.updateDataSet(items);
            if (items.size() >= getDataCountPrePage()) {
                adapter.setEndlessProgressItem(progressItem);
            } else {
                adapter.setEndlessProgressItem(null);
            }

            mHandler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false),
                    500L);
        } else if (rushState == UpdateState.LoadMore) {
            adapter.onLoadMoreComplete(items, -1);
        }
    }

    public int[] getColorSchemeResources() {
        return new int[]{
                android.R.color.holo_purple, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light};
    }

    public abstract AbstractFlexibleItem getItem(M model);

    /**
     * @param curPage 当前页数，从1开始
     */
    public abstract void getDataFromCache(UpdateState rushState, @Size(min = 1) int curPage,
                                          LoadListCallback<M> callback);

    /**
     * @param curPage 当前页数，从1开始
     */
    public abstract void getDataFromNet(UpdateState rushState, @Size(min = 1) int curPage,
                                        LoadListCallback<M> callback);

    public void iniEmptyView() {

    }

    public void onLoadMoreItemClick() {
        if (progressItem.getStatus() == ProgressItem.StatusEnum.ON_ERROR) {
            currentPage = currentPage + 1;
            getDataFromNet(UpdateState.LoadMore, currentPage, callbackLoad);
        }
    }

    /**
     * 每页数据量
     */
    public int getDataCountPrePage() {
        return 10;
    }
}
