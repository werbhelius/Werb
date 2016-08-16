package com.wanbo.werb.ui.presenter;

import android.content.Context;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.R;
import com.wanbo.werb.ui.view.ICARView;
import com.wanbo.werb.util.AccessTokenKeeper;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Comment and Repost with POST
 */
public class CARPresenter extends BasePresenter<ICARView> {

    private Context context;

    public CARPresenter(Context context) {
        this.context = context;
    }

    public void postComment(String text,String id){
        Oauth2AccessToken token = readToken(context);
        weiBoApi.setComment(getCommentMap(token.getToken(),text,id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postComments -> {
                    getView().finishAndToast();
                },this::loadError);
    }

    public void postRepost(String text,String id){
        Oauth2AccessToken token = readToken(context);
        weiBoApi.setRepost(getRepostMap(token.getToken(),text,id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    getView().finishAndToast();
                },this::loadError);
    }

    public void postCommentToReply(String text,String comment_id,String weibo_id){
        Oauth2AccessToken token = readToken(context);
        weiBoApi.setCommentToReply(getCommentToReplyMap(token.getToken(),text,comment_id,weibo_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    getView().finishAndToast();
                },this::loadError);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    // get request params
    private Map<String, Object> getCommentMap(String token, String comment,String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("comment", comment);
        map.put("id", id);
        return map;
    }

    private Map<String, Object> getRepostMap(String token, String comment,String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("status", comment);
        map.put("id", id);
        return map;
    }

    private Map<String, Object> getCommentToReplyMap(String token,String comment,String comment_id,String weibo_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("comment", comment);
        map.put("id", weibo_id);
        map.put("cid", comment_id);
        return map;
    }

    private Oauth2AccessToken readToken(Context context) {
        return AccessTokenKeeper.readAccessToken(context);
    }

}
