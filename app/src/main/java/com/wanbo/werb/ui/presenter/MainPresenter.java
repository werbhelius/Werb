package com.wanbo.werb.ui.presenter;


import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wanbo.werb.MyApp;
import com.wanbo.werb.bean.User;
import com.wanbo.werb.ui.view.IMainView;
import com.wanbo.werb.widget.ClickCircleImageView;

import java.util.ArrayList;

/**
 * Created by Werb on 2016/7/26.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * MainPresenter init some data
 */
public class MainPresenter extends BasePresenter<IMainView> {

    private FragmentActivity activity;
    private IMainView mainView;
    private ImageView userBackGround;
    private ClickCircleImageView userIcon;

    public MainPresenter(FragmentActivity activity) {
        this.activity = activity;
    }

    private User getUserInfoFromDB() {
        ArrayList<User> query = MyApp.mDb.query(User.class);
        if (query.size() > 0) {
            return query.get(0);
        } else {
            return null;
        }
    }

    public void setHeadViewWithUser() {
        User OAuthUser = getUserInfoFromDB();
        if (OAuthUser != null&&isViewAttached()) {
            mainView = getView();
            userBackGround = mainView.getUserBackGround();
            userIcon = mainView.getUserIcon();
            mainView.getUserName().setText(OAuthUser.getScreen_name());
            userIcon.setUserImage(OAuthUser);
            Glide.with(activity).load(OAuthUser.getCover_image_phone()).centerCrop().into(userBackGround);
        }
    }

    // else method maybe better ?
    public void destroyPic(){
        Glide.clear(userBackGround);
        Glide.clear(userIcon);
    }

}
