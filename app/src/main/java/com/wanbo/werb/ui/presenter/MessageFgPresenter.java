package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.Comments;
import com.wanbo.werb.bean.FriendsTimeLine;
import com.wanbo.werb.bean.MentionComment;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.ui.adapter.WeiBoListAdapter;
import com.wanbo.werb.ui.view.IMessageFgView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/8/11.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class MessageFgPresenter extends BasePresenter<IMessageFgView> {

    private Context context;
    private IMessageFgView messageFgView;
    private RecyclerView recyclerView;
    private WeiBoListAdapter adapter;
    private List<Status> listStatus = new ArrayList<>();
    private List<Comments> listComments = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private String tag;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public MessageFgPresenter(Context context) {
        this.context = context;
    }

    // @ 我的微博
    public void getMessageAtWeibo() {
        messageFgView = getView();
        if (messageFgView != null) {
            recyclerView = messageFgView.getRecyclerView();
            layoutManager = messageFgView.getLayoutManager();
            tag = messageFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            String count = "10";//just like it
            weiBoApi.getMessageAtWeiBo(getAtWeiBoMap(token.getToken(), count))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(friendsTimeLine -> {
                        disPlayWeiBoList(friendsTimeLine, context, messageFgView, recyclerView);
                    }, this::loadError);
        }
    }

    // @ 我的评论
    public void getMentionComment() {
        messageFgView = getView();
        if (messageFgView != null) {
            recyclerView = messageFgView.getRecyclerView();
            layoutManager = messageFgView.getLayoutManager();
            tag = messageFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getMessageAtComment(getAtCommentMap(token.getToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mentionComment -> {
                        disPlayAtCommentList(mentionComment, context, messageFgView, recyclerView);
                    }, this::loadError);
        }
    }

    // 收到的评论
    public void getMessageGetComment() {
        messageFgView = getView();
        if (messageFgView != null) {
            recyclerView = messageFgView.getRecyclerView();
            layoutManager = messageFgView.getLayoutManager();
            tag = messageFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getMessageGetComment(getAtCommentMap(token.getToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mentionComment -> {
                        disPlayAtCommentList(mentionComment, context, messageFgView, recyclerView);
                    }, this::loadError);
        }
    }

    // 发出的评论
    public void getMessageSendComment() {
        messageFgView = getView();
        if (messageFgView != null) {
            recyclerView = messageFgView.getRecyclerView();
            layoutManager = messageFgView.getLayoutManager();
            tag = messageFgView.getFgTag();
            Oauth2AccessToken token = readToken(context);
            weiBoApi.getMessageSendComment(getAtCommentMap(token.getToken()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mentionComment -> {
                        disPlayAtCommentList(mentionComment, context, messageFgView, recyclerView);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
        if (messageFgView != null) {
            messageFgView.setDataRefresh(false);
        }
    }

    // get request params
    private Map<String, Object> getAtWeiBoMap(String token, String count) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("count", count);
        return map;
    }

    // get request params
    String max_id;
    private Map<String, Object> getAtCommentMap(String token) {

        switch (tag) {
            case "at_comment":
                max_id = PrefUtils.getString(context, "at_comment_max_id", "0");
                break;
            case "get_comment":
                max_id = PrefUtils.getString(context, "get_comment_max_id", "0");
                break;
            case "send_comment":
                max_id = PrefUtils.getString(context, "send_comment_max_id", "0");
                break;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        if (isLoadMore) {
            map.put("max_id", Long.valueOf(max_id));
        }
        return map;
    }

    // refresh data
    private void disPlayWeiBoList(FriendsTimeLine friendsTimeLine, Context context, IMessageFgView messageFgView, RecyclerView recyclerView) {
        listStatus = friendsTimeLine.getStatuses();
        adapter = new WeiBoListAdapter(context, listStatus, "home_fg");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        messageFgView.setDataRefresh(false);
    }

    private void disPlayAtCommentList(MentionComment mentionComment, Context context, IMessageFgView messageFgView, RecyclerView recyclerView) {
        List<Comments> comments = mentionComment.getComments();
        if (isLoadMore) {
            if (max_id.equals("0")) {
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                messageFgView.setDataRefresh(false);
                return;
            }
            if (comments.size() == 0||comments.size()==1) {
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                messageFgView.setDataRefresh(false);
                return;
            } else {
                listComments.addAll(comments.subList(1, comments.size() - 1));
            }
            adapter.notifyDataSetChanged();
        } else {
            listComments = comments;
            adapter = new WeiBoListAdapter(context, listComments, "message_at_comment");
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        messageFgView.setDataRefresh(false);
        // save max_id
        if (comments.size() > 0) {
            switch (tag) {
                case "at_comment":
                    PrefUtils.setString(context, "at_comment_max_id", comments.get(comments.size() - 1).getIdstr());
                    break;
                case "get_comment":
                    PrefUtils.setString(context, "get_comment_max_id", comments.get(comments.size() - 1).getIdstr());
                    break;
                case "send_comment":
                    PrefUtils.setString(context, "send_comment_max_id", comments.get(comments.size() - 1).getIdstr());
                    break;
            }
        }
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
                                case "at_weibo":

                                    break;
                                case "at_comment":
                                    getMentionComment();
                                    break;
                                case "get_comment":
                                    getMessageGetComment();
                                    break;
                                case "send_comment":
                                    getMessageSendComment();
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
