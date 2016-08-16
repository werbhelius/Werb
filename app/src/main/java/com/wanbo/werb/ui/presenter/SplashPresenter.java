package com.wanbo.werb.ui.presenter;

import android.app.Activity;
import android.content.Context;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.MyApp;
import com.wanbo.werb.bean.User;
import com.wanbo.werb.ui.view.ISplashView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.util.StateUtils;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Werb on 2016/7/26.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class SplashPresenter extends BasePresenter<ISplashView> {

    private Activity activity;

    public SplashPresenter(Activity activity) {
        this.activity = activity;
    }

    public void getUserInfo(){
        Oauth2AccessToken token = readToken(activity);
        if(token.isSessionValid()){
            String tokenStr = token.getToken();
            String uId = token.getUid();
            weiBoApi.getUserInfo(getRequestMap(tokenStr,uId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(this::saveUserInfo)
                    .subscribe(user -> {

                    }, this::loadError);
        }
    }


    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if(StateUtils.isNetworkAvailable(activity)){
            requestDataRefresh();
        }
    }


    public void requestDataRefresh() {
        getUserInfo();
    }

    private Oauth2AccessToken readToken(Context context){
        return AccessTokenKeeper.readAccessToken(context);
    }

    private Map<String,String> getRequestMap(String token,String uId){
        Map<String,String> map = new HashMap<>();
        map.put("access_token",token);
        map.put("uid",uId);
        return map;
    }

    private void saveUserInfo(User OAuthUser){
        MyApp.mDb.insert(OAuthUser, ConflictAlgorithm.Replace);
    }
}
