package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.BuildConfig;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.FriendsTimeLine;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.IHomeView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class HomePresenter extends BasePresenter<IHomeView> {

    private static final String TAG = "HomePresenter";

    private Context context;
    private IHomeView homeView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<Status> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public HomePresenter(Context ctx) {
        this.context = ctx;
    }

    public void getWeiBoTimeLine() {
        homeView = getHomeView();
        if (homeView != null) {
            recyclerView = homeView.getRecyclerView();
            layoutManager = homeView.getLayoutManager();

            Oauth2AccessToken token = readToken(context);
            String count = "10";//just like it
            if (token.isSessionValid()) {
                String tokenStr = token.getToken();
                weiBoApi.getFriendsTimeLine(getRequestMap(tokenStr, count))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(friendsTimeLine -> {
                            disPlayWeiBoList(friendsTimeLine, context, homeView, recyclerView);
                        }, this::loadError);
            }
        }
    }

    // 点赞POST请求
    public void setLike(String id){
        Oauth2AccessToken token = readToken(context);
        weiBoApi.setLike(getLikeMap(token.getToken(),id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postComments -> {

                });
    }

    private IHomeView getHomeView() {
        if (isViewAttached()) {
            return getView();
        } else {
            return null;
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
        if (homeView != null) {
            homeView.setDataRefresh(false);
        }
    }

    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }

    String max_id;
    // get request params
    private Map<String, Object> getRequestMap(String token, String count) {
        max_id = PrefUtils.getString(context, "max_id", "0");
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("count", count);
        if (isLoadMore) {
            map.put("max_id", Long.valueOf(max_id));
        }
        return map;
    }

    private Map<String, Object> getLikeMap(String token, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("attitude", "simle");
        map.put("id", id);
        return map;
    }

    // refresh data
    private void disPlayWeiBoList(FriendsTimeLine friendsTimeLine, Context context, IHomeView homeView, RecyclerView recyclerView) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, friendsTimeLine.toString());
        }
        if (isLoadMore) {
            if(max_id.equals("0")){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                return;
            }
            list.addAll(getStatusData(friendsTimeLine));
            adapter.notifyDataSetChanged();
        } else {
            list = getStatusData(friendsTimeLine);
            adapter = new WeiBoListAdapter(context, list,"home_fg");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        homeView.setDataRefresh(false);
        // save max_id
        PrefUtils.setString(context, "max_id", friendsTimeLine.getMax_id());
    }

    private List<Status> getStatusData(FriendsTimeLine friendsTimeLine) {
        return friendsTimeLine.getStatuses();
    }

    /**
     * recyclerView Scroll listener , maybe in here is wrong ?
     */
    public void scrollRecycleView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        adapter.updateLoadStatus(adapter.LOAD_PULL_TO);
                        isLoadMore = true;
                        adapter.updateLoadStatus(adapter.LOAD_MORE);
                        new Handler().postDelayed(() -> getWeiBoTimeLine(), 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

}
