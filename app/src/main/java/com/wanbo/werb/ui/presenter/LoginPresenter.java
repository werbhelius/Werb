package com.wanbo.werb.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wanbo.werb.R;
import com.wanbo.werb.info.Constants;
import com.wanbo.werb.ui.view.ILoginView;
import com.wanbo.werb.util.AccessTokenKeeper;

/**
 * Created by Werb on 2016/7/25.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Presenter about Login
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private ILoginView loginView;
    private Activity activity;

    public LoginPresenter(Activity activity) {
        this.activity = activity;
    }

    public void initRequestInfo(){
        mAuthInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
    }

    //回调
    public void ssoCallBack(int requestCode, int resultCode, Intent data){
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    //登陆
    public void login(){
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                //parse token from bundle
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                //show token
                if(isViewAttached()){
                    loginView = getView();
                    loginView.showTokenInfo(false,mAccessToken);
                }
                //save token
                writeToken(mAccessToken,activity);

                Toast.makeText(activity,
                        R.string.weibo_auth_success, Toast.LENGTH_SHORT).show();
                //go to home
                loginView.toMainActivity();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(activity,
                       R.string.weibo_auth_exception, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity,
                        R.string.weibo_auth_canceled, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserInfo(){
        new SplashPresenter(activity).getUserInfo();
    }

    private void writeToken(Oauth2AccessToken mAccessToken,Context context){
        AccessTokenKeeper.writeAccessToken(context, mAccessToken);
    }

    public Oauth2AccessToken readToken(Context context){
        return AccessTokenKeeper.readAccessToken(context);
    }
}
