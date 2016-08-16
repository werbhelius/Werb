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
import com.wanbo.werb.bean.Comments;
import com.wanbo.werb.bean.CommentsTimeLine;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.ITabView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.PrefUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class TabPresenter extends BasePresenter<ITabView> {

    private static final String TAG = "TabPresenter";

    private Context context;
    private String weibo_id;
    private ITabView tabView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<Comments> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多


    public TabPresenter(Context context) {
        this.context = context;
    }

    public void getComments(){
        tabView = getTabView();
        if(tabView!=null){
            weibo_id = tabView.getWeiBoId();
            recyclerView = tabView.getRecyclerView();
            layoutManager = tabView.getLayoutManager();
        }

        Oauth2AccessToken token = readToken(context);
        String count = "10";//just like it
        System.out.println("--taken--"+token.getToken());
        if (token.isSessionValid()) {
            weiBoApi.getCommentsById(getRequestMap(token.getToken(),count,weibo_id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(commentsTimeLine -> {
                        disPlayWeiBoList(commentsTimeLine,context,tabView,recyclerView);
                    },this::loadError);
        }
    }

    private ITabView getTabView() {
        if (isViewAttached()) {
            return getView();
        } else {
            return null;
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context,R.string.load_error,Toast.LENGTH_SHORT).show();
    }

    String max_id;
    // get request params
    private Map<String, Object> getRequestMap(String token, String count,String id) {
        max_id = PrefUtils.getString(context, "comment_max_id", "0");
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("count", count);
        map.put("id", id);
        if (isLoadMore) {
            map.put("max_id", Long.valueOf(max_id));
        }
        return map;
    }

    // refresh data
    private void disPlayWeiBoList(CommentsTimeLine commentsTimeLine, Context context, ITabView tabView, RecyclerView recyclerView) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, commentsTimeLine.toString());
        }
        if (isLoadMore) {
            if(max_id.equals("0")){
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                return;
            }
            list.addAll(getCommentsData(commentsTimeLine));
            adapter.notifyDataSetChanged();
        } else {
            list = getCommentsData(commentsTimeLine);
            adapter = new WeiBoListAdapter(context, list,"tab_fg");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        // save max_id
        PrefUtils.setString(context, "comment_max_id", commentsTimeLine.getMax_id());
    }

    private List<Comments> getCommentsData(CommentsTimeLine commentsTimeLine) {
        return commentsTimeLine.getComments();
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
                        new Handler().postDelayed(() -> getComments(),1500);
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
