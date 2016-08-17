package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.FriendShips;
import com.wanbo.werb.bean.User;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.IFriendFgView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class FriendShipsFgPresenter extends BasePresenter<IFriendFgView> {

    private Context context;
    private IFriendFgView friendFgView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<User> listFriend = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private String tag;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public FriendShipsFgPresenter(Context context) {
        this.context = context;
    }

    // 关注
    public void getFriendById(String uid){
        friendFgView = getView();
        if (friendFgView != null) {
            recyclerView = friendFgView.getRecyclerView();
            layoutManager = friendFgView.getLayoutManager();
            tag = friendFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getFriendById(getRequestMap(token.getToken(),uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(friendShips -> {
                        disPlayFriendsList(friendShips,context,friendFgView,recyclerView);
                    }, this::loadError);
        }
    }

    // 粉丝
    public void getFollowerById(String uid){
        friendFgView = getView();
        if (friendFgView != null) {
            recyclerView = friendFgView.getRecyclerView();
            layoutManager = friendFgView.getLayoutManager();
            tag = friendFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getFollowerById(getRequestMap(token.getToken(),uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(friendShips -> {
                        disPlayFriendsList(friendShips,context,friendFgView,recyclerView);
                    }, this::loadError);
        }
    }

    // 互粉
    public void getBilateralById(String uid){
        friendFgView = getView();
        if (friendFgView != null) {
            recyclerView = friendFgView.getRecyclerView();
            layoutManager = friendFgView.getLayoutManager();
            tag = friendFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getBilateralById(getRequestMap(token.getToken(),uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(friendShips -> {
                        disPlayFriendsList(friendShips,context,friendFgView,recyclerView);
                    }, this::loadError);
        }
    }


    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
        if (friendFgView != null) {
            friendFgView.setDataRefresh(false);
        }
    }

    // get request params
    String max_id;
    private Map<String, Object> getRequestMap(String token,String uid) {
        switch (tag) {
            case "friends":
                max_id = PrefUtils.getString(context, "friends_next_cursor", "0");
                break;
            case "followers":
                max_id = PrefUtils.getString(context, "followers_next_cursor", "0");
                break;
            case "bilateral":
                max_id = PrefUtils.getString(context, "bilateral_next_cursor", "0");
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("uid", uid);
        if (isLoadMore) {
            map.put("cursor", Long.valueOf(max_id));
        }
        return map;
    }

    private void disPlayFriendsList(FriendShips friendShips, Context context, IFriendFgView friendFgView, RecyclerView recyclerView) {
        List<User> users = friendShips.getUsers();
        if (isLoadMore) {
            if (max_id.equals("0")) {
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                friendFgView.setDataRefresh(false);
                return;
            }
            if (users.size() == 0||users.size()==1) {
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                friendFgView.setDataRefresh(false);
                return;
            } else {
                listFriend.addAll(users.subList(1, users.size() - 1));
            }
            adapter.notifyDataSetChanged();
        } else {
            listFriend = users;
            adapter = new WeiBoListAdapter(context, listFriend, "friend_ship");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        friendFgView.setDataRefresh(false);
        // save max_id
        if (users.size() > 0) {
            switch (tag) {
                case "friends":
                    PrefUtils.setString(context, "friends_next_cursor",friendShips.getNext_cursor());
                    break;
                case "followers":
                    PrefUtils.setString(context, "followers_next_cursor", friendShips.getNext_cursor());
                    break;
                case "bilateral":
                    PrefUtils.setString(context, "bilateral_next_cursor", friendShips.getNext_cursor());
                    break;
            }
        }
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
                    if(layoutManager.getItemCount()==1){
                        adapter.updateLoadStatus(adapter.LOAD_NONE);
                        return;
                    }
                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        adapter.updateLoadStatus(adapter.LOAD_PULL_TO);
                        isLoadMore = true;
                        adapter.updateLoadStatus(adapter.LOAD_MORE);
                        new Handler().postDelayed(() -> {
                            switch (tag) {
                                case "friends":
                                    getFriendById(uid);
                                    break;
                                case "followers":
                                    getFollowerById(uid);
                                    break;
                                case "bilateral":
                                    getBilateralById(uid);
                                    break;
                            }
                        }, 1000);
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

    // get token
    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }
}
