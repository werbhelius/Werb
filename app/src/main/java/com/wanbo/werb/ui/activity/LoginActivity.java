package com.wanbo.werb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.BuildConfig;
import com.wanbo.werb.R;
import com.wanbo.werb.ui.presenter.LoginPresenter;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.view.ILoginView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Werb on 2016/7/25.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Login WeiBo By OAuth2
 */
public class LoginActivity extends MVPBaseActivity<ILoginView,LoginPresenter> implements ILoginView {

    private static final String TAG = "LoginActivity";
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPresenter.initRequestInfo();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login) void loginWeibo(){
        mPresenter.login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //sso callBack
        mPresenter.ssoCallBack(requestCode,resultCode,data);

        //read token
        mAccessToken = mPresenter.readToken(this);
        if (mAccessToken.isSessionValid()) {
            showTokenInfo(true,mAccessToken);
        }
    }

    @Override
    public void showTokenInfo(boolean hasExisted,Oauth2AccessToken mAccessToken) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibo_token_to_string_format_1);
        if(BuildConfig.DEBUG){
            Log.d(TAG, "---Token---"+String.format(format, mAccessToken.getToken(), date));
        }
        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibo_token_has_existed) + "\n" + message;
        }
        if(BuildConfig.DEBUG){
            Log.d(TAG, "---Token---"+message);
        }
    }

    @Override
    public void toMainActivity() {
        mPresenter.getUserInfo();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
