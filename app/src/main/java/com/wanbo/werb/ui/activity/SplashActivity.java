package com.wanbo.werb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wanbo.werb.BuildConfig;
import com.wanbo.werb.R;
import com.wanbo.werb.ui.presenter.SplashPresenter;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.view.ISplashView;
import com.wanbo.werb.util.AccessTokenKeeper;
import com.wanbo.werb.widget.splash.SplashView;
import java.util.Random;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Werb on 2016/7/25.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * SplashActivity like Twitter !
 */
public class SplashActivity extends MVPBaseActivity<ISplashView,SplashPresenter> implements ISplashView{

    private static final String TAG = "SplashActivity";

    private Handler mHandler = new Handler();

    @Bind(R.id.splash_view) SplashView splash_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPresenter = createPresenter();
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(SplashActivity.this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getUserInfo();
        startLoadingData();
    }

    /**
     * start splash animation
     */
    private void startLoadingData(){
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run(){
                onLoadingDataEnded();
            }
        }, 1000 + random.nextInt(2000));
    }

    private void onLoadingDataEnded(){
        // start the splash animation
        splash_view.splashAndDisappear(new SplashView.ISplashListener(){
            @Override
            public void onStart(){
                // log the animation start event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction){
                // log animation update events
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd(){
                // log the animation end event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash ended");
                }
                // free the view so that it turns into garbage
                splash_view = null;
                loginOrMain();
            }
        });
    }

    @Override
    public void loginOrMain() {
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(SplashActivity.this);
        if (token.isSessionValid()) {
            if(BuildConfig.DEBUG){
                Log.d(TAG,token.toString()+"--uid-"+token.getUid());
            }
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

}
