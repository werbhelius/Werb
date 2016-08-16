package com.wanbo.werb.ui.presenter;

import android.content.Context;

import com.wanbo.werb.ui.view.IWeiBoDetailView;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class WeiBoDetailPresenter extends BasePresenter<IWeiBoDetailView> {

    private Context context;

    public WeiBoDetailPresenter(Context context) {
        this.context = context;
    }
}
