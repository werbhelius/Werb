package com.wanbo.werb.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.presenter.HomePresenter;
import com.wanbo.werb.ui.activity.SendWeiBoActivity;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.view.IHomeView;
import com.wanbo.werb.util.RxBus;
import com.wanbo.werb.util.RxEvents;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * fragment of home weibo to display
 */
public class HomeFragment extends MVPBaseFragment<IHomeView, HomePresenter> implements IHomeView {

    private LinearLayoutManager mLayoutManager;

    @Bind(R.id.content_list)
    RecyclerView mRecyclerView;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getWeiBoTimeLine();
        mPresenter.scrollRecycleView();

        RxBus.getInstance().toObserverable().subscribe(event->{
            if(event instanceof RxEvents.UpRefreshClick){
                mRecyclerView.smoothScrollToPosition(0);
                requestDataRefresh();
            }else if(event instanceof RxEvents.WeiBoSetLike){
                RxEvents.WeiBoSetLike like = (RxEvents.WeiBoSetLike) event;
            }
        });
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getWeiBoTimeLine();
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    //发微博
    @OnClick(R.id.send_weibo) void sendWeibo(){
        startActivity(new Intent(getActivity(), SendWeiBoActivity.class));
    }
}
