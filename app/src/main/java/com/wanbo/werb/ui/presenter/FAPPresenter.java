package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.Favorite;
import com.wanbo.werb.bean.Favorites;
import com.wanbo.werb.bean.FriendsTimeLine;
import com.wanbo.werb.bean.Photo;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.IFAPView;
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
public class FAPPresenter extends BasePresenter<IFAPView> {

    private Context context;
    private IFAPView ifapView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<Favorite> list = new ArrayList<>();
    private List<Photo> photoList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private String tag;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public FAPPresenter(Context context) {
        this.context = context;
    }

    public void getFavorites() {
        ifapView = getView();
        if (ifapView != null) {
            recyclerView = ifapView.getRecyclerView();
            layoutManager = ifapView.getLayoutManager();
            tag = ifapView.getTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getFavorites(getRequestMap(token.getToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(favorites -> {
                        disPlayWeiBoList(favorites,context,ifapView,recyclerView);
                    }, this::loadError);
        }
    }

    public void getPhoto(String uid){
        ifapView = getView();
        if (ifapView != null) {
            recyclerView = ifapView.getRecyclerView();
            gridLayoutManager = ifapView.getGridLayoutManager();
            tag = ifapView.getTag();
            Oauth2AccessToken token = readToken(context);
            if (token.isSessionValid()) {
                weiBoApi.getUserWeiBoTimeLine(getRequestMap(token.getToken(),uid))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(friendsTimeLine -> {
                            List<Photo> photoList = setPhotoList(friendsTimeLine);
                            disPlayPhotoList(photoList,context,ifapView,recyclerView);
                        },this::loadError);
            }

        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
        if (ifapView != null) {
            ifapView.setDataRefresh(false);
        }
    }

    String max_id;
    // get request params
    private Map<String, Object> getRequestMap(String token, String uid) {
        max_id = PrefUtils.getString(context, "user_photo_max_id", "0");
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("uid", uid);
        if (isLoadMore) {
            map.put("max_id", Long.valueOf(max_id));
        }
        return map;
    }

    int page = 1;
    private Map<String, Object> getRequestMap(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("page", page);
        return map;
    }

    private void disPlayWeiBoList(Favorites favorites, Context context, IFAPView ifapView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if(favorites == null){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                return;
            }
            list.addAll(favorites.getFavorites());
            adapter.notifyDataSetChanged();
        } else {
            list = favorites.getFavorites();
            adapter = new WeiBoListAdapter(context, list,"favorites");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        ifapView.setDataRefresh(false);
        // save page
        page = page +1;
    }

    private void disPlayPhotoList(List<Photo> photos, Context context, IFAPView ifapView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if(photoList == null){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                return;
            }
            photoList.addAll(photos);
            adapter.notifyDataSetChanged();
        } else {
            photoList = photos;
            adapter = new WeiBoListAdapter(context, photoList,"user_photo");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        ifapView.setDataRefresh(false);

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
                                case "favorites":
                                    getFavorites();
                                    break;
                                case "photo":
                                    getPhoto(uid);
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

    /**
     * 得到相册集合
     * @param friendsTimeLine
     * @return
     */
    private List<Photo> setPhotoList(FriendsTimeLine friendsTimeLine){
        List<Photo> photoList = new ArrayList<>();
        List<Status> statusList = new ArrayList<>();
        List<Status> statuses = friendsTimeLine.getStatuses();
        for (Status s:statuses) {
            if(s.getRetweeted_status()==null){
                statusList.add(s);
            }
        }
        for (Status s:statusList) {
            ArrayList<Status.ThumbnailPic> pic_urls = s.getPic_urls();
            for (Status.ThumbnailPic t: pic_urls) {
                Photo p = new Photo();
                p.setPic_url(t.getImage());
                p.setText(s.getText());
                photoList.add(p);
            }
        }
        // save max_id
        PrefUtils.setString(context, "user_photo_max_id", statuses.get(statuses.size()-1).getIdstr());
        return photoList;
    }


    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }
}
