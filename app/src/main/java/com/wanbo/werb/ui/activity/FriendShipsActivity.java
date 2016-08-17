package com.wanbo.werb.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.adapter.ViewPagerFgAdapter;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.fragment.FriendShipFragment;
import com.wanbo.werb.ui.presenter.FriendShipsPresenter;
import com.wanbo.werb.ui.view.IFriendsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Friends and Followers
 */
public class FriendShipsActivity extends MVPBaseActivity<IFriendsView,FriendShipsPresenter> implements IFriendsView {

    private static final String TAG = "tag";
    private static final String UID = "uid";

    private List<MVPBaseFragment> fragmentList;
    private String tag;
    private String uid;

    @Bind(R.id.friend_tabLayout)
    TabLayout friend_tabLayout;
    @Bind(R.id.friend_viewPager)
    ViewPager friend_viewPager;

    @Override
    protected FriendShipsPresenter createPresenter() {
        return new FriendShipsPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_friend_ship;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        initTabView();
    }

    public static Intent newIntent(Context context,String tag,String uid) {
        Intent intent = new Intent(context, FriendShipsActivity.class);
        intent.putExtra(FriendShipsActivity.TAG,tag);
        intent.putExtra(FriendShipsActivity.UID,uid);
        return intent;
    }

    private void parseIntent() {
        tag = getIntent().getStringExtra(TAG);
        uid = getIntent().getStringExtra(UID);
    }

    //初始化Tab滑动
    public void initTabView(){
        fragmentList = new ArrayList<>();
        fragmentList.add(FriendShipFragment.newInstance("friends",uid));
        fragmentList.add(FriendShipFragment.newInstance("followers",uid));
        fragmentList.add(FriendShipFragment.newInstance("bilateral",uid));

        friend_viewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(),fragmentList,"FriendShips"));//给ViewPager设置适配器
        friend_tabLayout.setupWithViewPager(friend_viewPager);//将TabLayout和ViewPager关联起来

        switch (tag) {
            case "friends":
                friend_viewPager.setCurrentItem(0);
                break;
            case "followers":
                friend_viewPager.setCurrentItem(1);
                break;
            case "bilateral":
                friend_viewPager.setCurrentItem(2);
                break;
        }
    }
}
