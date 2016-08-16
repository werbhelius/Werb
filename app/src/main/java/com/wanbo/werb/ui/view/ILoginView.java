package com.wanbo.werb.ui.view;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Werb on 2016/7/25.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public interface ILoginView {

    void showTokenInfo(boolean hasExisted,Oauth2AccessToken mAccessToken);
    void toMainActivity();
}
