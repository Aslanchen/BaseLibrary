package com.aslan.baselibrary.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.executor.AppTaskExecutor;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.items.ProgressItem;
import com.aslan.baselibrary.listener.LoadListCallback;
import com.aslan.baselibrary.view.EmptyView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.helpers.ActionModeHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表界面统一封装
 *
 * @author Aslanchen
 * @date 017/4/27.
 */
public abstract class BaseRecyleListFragment<M> extends BaseFragment implements
    FlexibleAdapter.OnUpdateListener, FlexibleAdapter.EndlessScrollListener,
    FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnItemLongClickListener,
    FlexibleAdapter.OnItemSubClickListener, SwipeRefreshLayout.OnRefreshListener {

  public enum UpdateState {
    Refresh,
    LoadMore
  }

  protected int curPage = 1;
  protected int perPage = 10;
  protected FlexibleAdapter<AbstractFlexibleItem> adapter;
  protected ProgressItem progressItem = new ProgressItem();

  protected SwipeRefreshLayout swipeRefreshLayout;
  protected RecyclerView recyclerView;
  protected EmptyView listEmptyView;
  protected ActionModeHelper mActionModeHelper;

  private LoadListCallback<M> callbackRefresh;
  private LoadListCallback<M> callbackLoad;

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

    iniPage();
    iniEmptyView();

    swipeRefreshLayout.setColorSchemeResources(
        android.R.color.holo_purple, android.R.color.holo_blue_light,
        android.R.color.holo_green_light, android.R.color.holo_orange_light);
    swipeRefreshLayout.setEnabled(true);

    recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    adapter = new FlexibleAdapter<AbstractFlexibleItem>(new ArrayList<AbstractFlexibleItem>(),
        this);
    adapter.setEndlessScrollListener(this, progressItem);
    adapter.setEndlessPageSize(perPage);
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
      public void onDataNotAvailable(BaseHttpError error) {
        recyclerView.postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeRefreshLayout.setRefreshing(false);
          }
        }, 500L);
      }
    };

    callbackLoad = new LoadListCallback<M>() {
      @Override
      public void onLoaded(@NonNull List<M> respones) {
        addToListView(UpdateState.LoadMore, respones);
      }

      @Override
      public void onDataNotAvailable(BaseHttpError error) {
        recyclerView.postDelayed(new Runnable() {
          @Override
          public void run() {
            adapter.onLoadMoreComplete(null);
            curPage--;
          }
        }, 500L);
      }
    };

    getDataFromDB(new LoadListCallback<M>() {
      @Override
      public void onLoaded(@NonNull List<M> respones) {
        addToListView(UpdateState.Refresh, respones);
        autoRefreshDelay();
      }

      @Override
      public void onDataNotAvailable(BaseHttpError error) {
        autoRefreshDelay();
      }
    });
  }

  protected void autoRefreshDelay() {
    AppTaskExecutor.getInstance().postToMainThreadDelayed(() -> {
      autoRefresh();
    }, 500L);
  }

  protected void autoRefresh() {
    swipeRefreshLayout.setRefreshing(true);
    onRefresh();
  }

  @CallSuper
  @Override
  public void onRefresh() {
    curPage = 1;
    getDataFromNet(UpdateState.Refresh, callbackRefresh);
  }

  @Override
  public void noMoreLoad(int newItemsSize) {

  }

  @CallSuper
  @Override
  public void onLoadMore(int lastPosition, int currentPage) {
    if (lastPosition < perPage) {
      adapter.onLoadMoreComplete(null);
      return;
    }

    curPage++;
    getDataFromNet(UpdateState.LoadMore, callbackLoad);
  }

  @Override
  public boolean onItemClick(int position) {
    return false;
  }

  @Override
  public void onItemLongClick(int position) {

  }

  @Override
  public boolean onItemSubClick(int position, View view) {
    return false;
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

    AppTaskExecutor.getInstance().executeOnMainThread(() -> {
      if (rushState == UpdateState.Refresh) {
        adapter.updateDataSet(items);
        adapter.setEndlessProgressItem(progressItem);

        AppTaskExecutor.getInstance()
            .postToMainThreadDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500L);
      } else if (rushState == UpdateState.LoadMore) {
        adapter.onLoadMoreComplete(items, 500L);
      }
    });
  }

  /**
   * 设置颜色值
   */
  private void setColorSchemeResources(SwipeRefreshLayout swipeRefreshLayout) {
    swipeRefreshLayout.setColorSchemeResources(
        android.R.color.holo_purple, android.R.color.holo_blue_light,
        android.R.color.holo_green_light, android.R.color.holo_orange_light);
  }

  public abstract AbstractFlexibleItem getItem(M model);

  public abstract void getDataFromDB(LoadListCallback<M> callback);

  public abstract void getDataFromNet(UpdateState rushState, LoadListCallback<M> callback);

  public void iniEmptyView() {

  }

  public void iniPage() {

  }
}
