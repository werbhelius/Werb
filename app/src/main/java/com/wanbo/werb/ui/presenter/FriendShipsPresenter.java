package com.wanbo.werb.ui.presenter;

import android.content.Context;

import com.wanbo.werb.ui.view.IFriendsView;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class FriendShipsPresenter extends BasePresenter<IFriendsView> {

    private Context context;

    public FriendShipsPresenter(Context context) {
        this.context = context;
    }
}
