package com.wanbo.werb.ui.presenter;

import android.content.Context;

import com.wanbo.werb.ui.view.IMessageView;

/**
 * Created by Werb on 2016/8/11.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class MessagePresenter extends BasePresenter<IMessageView> {

    private Context context;

    public MessagePresenter(Context context) {
        this.context = context;
    }
}
