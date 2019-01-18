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
import eu.davidea.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.helpers.ActionModeHelper;
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

  private LoadListCallback<M> callbackRefresh;
  private LoadListCallback<M> callbackLoad;
  private int currentPage = 1;

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

    setColorSchemeResources(swipeRefreshLayout);
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
      public void onDataNotAvailable(BaseHttpError error) {
        recyclerView.post(new Runnable() {
          @Override
          public void run() {
            showToastMessage(error.getMsg());
            swipeRefreshLayout.setRefreshing(false);
          }
        });
      }
    };

    callbackLoad = new LoadListCallback<M>() {
      @Override
      public void onLoaded(@NonNull List<M> respones) {
        addToListView(UpdateState.LoadMore, respones);
      }

      @Override
      public void onDataNotAvailable(BaseHttpError error) {
        recyclerView.post(new Runnable() {
          @Override
          public void run() {
            showToastMessage(error.getMsg());
            loadFialed();
          }
        });
      }
    };

    getDataFromDB(UpdateState.Refresh, 1, new LoadListCallback<M>() {
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
    currentPage = 1;
    getDataFromNet(UpdateState.Refresh, 1, callbackRefresh);
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
    if (progressItem != null) {
      progressItem.setStatus(ProgressItem.StatusEnum.ON_ERROR);
    }
  }

  /**
   * item点击，需要注意的是，最外层view不能设置id，因为需要使用view.getId() == -1来做判断,可以使用itemView来替代使用进行样式或者值得设置
   *
   * @param view the view that generated the event
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

    AppTaskExecutor.getInstance().executeOnMainThread(() -> {
      if (rushState == UpdateState.Refresh) {
        adapter.updateDataSet(items);
        if (items.size() >= getDataCountPrePage()) {
          adapter.setEndlessProgressItem(progressItem);
        }

        AppTaskExecutor.getInstance()
            .postToMainThreadDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500L);
      } else if (rushState == UpdateState.LoadMore) {
        adapter.onLoadMoreComplete(items, -1);
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

  /**
   * @param curPage 当前页数，从1开始
   */
  public abstract void getDataFromDB(UpdateState rushState, int curPage,
      LoadListCallback<M> callback);

  /**
   * @param curPage 当前页数，从1开始
   */
  public abstract void getDataFromNet(UpdateState rushState, int curPage,
      LoadListCallback<M> callback);

  public void iniEmptyView() {

  }

  public void onLoadMoreItemClick() {
    if (progressItem.getStatus() == ProgressItem.StatusEnum.ON_ERROR) {
      getDataFromNet(UpdateState.LoadMore, currentPage + 1, callbackLoad);
    }
  }

  /**
   * 每页数据量
   */
  public int getDataCountPrePage() {
    return 10;
  }
}
