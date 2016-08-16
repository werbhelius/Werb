package com.wanbo.werb.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.adapter.ViewPagerFgAdapter;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.fragment.MessageFragment;
import com.wanbo.werb.ui.presenter.MessagePresenter;
import com.wanbo.werb.ui.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/11.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Message of Activity
 */
public class MessageActivity extends MVPBaseActivity<IMessageView,MessagePresenter> implements IMessageView {

    private List<MVPBaseFragment> fragmentList;

    @Bind(R.id.message_tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.message_viewPager)
    ViewPager mViewPager;

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabView();
    }

    //初始化Tab滑动
    public void initTabView(){

        fragmentList = new ArrayList<>();
        fragmentList.add(MessageFragment.newInstance("at_weibo"));
        fragmentList.add(MessageFragment.newInstance("at_comment"));
        fragmentList.add(MessageFragment.newInstance("get_comment"));
        fragmentList.add(MessageFragment.newInstance("send_comment"));

        mViewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(),fragmentList,"Message"));//给ViewPager设置适配器
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来
    }
}
