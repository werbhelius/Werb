package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.FriendsTimeLine;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.IUserView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/7/31.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class UserPresenter extends BasePresenter<IUserView> {

    private Context context;
    private IUserView userView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<Status> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public UserPresenter(Context context) {
        this.context = context;
    }

    public void getUserWeiBoTimeLine(String uid){
        userView = getView();
        if(userView != null){
            recyclerView = userView.getRecyclerView();
            layoutManager = userView.getLayoutManager();
            Oauth2AccessToken token = readToken(context);
            if (token.isSessionValid()) {
                weiBoApi.getUserWeiBoTimeLine(getRequestMap(token.getToken(),uid))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(friendsTimeLine -> {
                            disPlayWeiBoList(friendsTimeLine,context,userView,recyclerView);
                        },this::loadError);
            }
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
        if (userView != null) {
            userView.setDataRefresh(false);
        }
    }

    String max_id;
    // get request params
    private Map<String, Object> getRequestMap(String token, String uid) {
        max_id = PrefUtils.getString(context, "user_max_id", "0");
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("uid", uid);
        if (isLoadMore) {
            map.put("max_id", Long.valueOf(max_id));
        }
        return map;
    }

    // refresh data
    private void disPlayWeiBoList(FriendsTimeLine friendsTimeLine, Context context, IUserView userView, RecyclerView recyclerView) {
        List<Status> statuses = friendsTimeLine.getStatuses();
        if (isLoadMore) {
            if(max_id.equals("0")){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                return;
            }
            if (statuses.size() == 0 ){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                userView.setDataRefresh(false);
                return;
            }else if(statuses.size()==1){
                list.addAll(statuses);
            } else{
                list.addAll(statuses.subList(1,statuses.size()-1));
            }
            adapter.notifyDataSetChanged();
        } else {
            list = friendsTimeLine.getStatuses();
            adapter = new WeiBoListAdapter(context, list,"home_fg");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        userView.setDataRefresh(false);
        // save max_id
        PrefUtils.setString(context, "user_max_id", statuses.get(statuses.size()-1).getIdstr());
    }

    /**
     * recyclerView Scroll listener , maybe in here is wrong ?
     */
    public void scrollRecycleView(String uid) {
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
                        new Handler().postDelayed(() -> getUserWeiBoTimeLine(uid), 1000);
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



    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }
}
