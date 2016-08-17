package com.wanbo.werb.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.presenter.FriendShipsFgPresenter;
import com.wanbo.werb.ui.view.IFriendFgView;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * PS:由于权限原因所以未能获取全部数据
 */
public class FriendShipFragment extends MVPBaseFragment<IFriendFgView, FriendShipsFgPresenter> implements IFriendFgView {

    private static final String TAG = "tag";
    private static final String UID = "uid";
    private String tag;
    private String uid;

    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.friend_list)
    RecyclerView friend_list;

    @Override
    protected FriendShipsFgPresenter createPresenter() {
        return new FriendShipsFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_friend_ship;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        uid = getArguments().getString(UID);
    }

    public static FriendShipFragment newInstance(String tag, String uid) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        args.putString(FriendShipFragment.TAG, tag);
        args.putString(FriendShipFragment.UID, uid);
        FriendShipFragment fragment = new FriendShipFragment();
        //将Bundle设置为fragment的参数
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        friend_list.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (tag) {
            case "friends":
                setDataRefresh(true);
                mPresenter.getFriendById(uid);
                mPresenter.scrollRecycleView(uid);
                break;
            case "followers":
                setDataRefresh(true);
                mPresenter.getFollowerById(uid);
                mPresenter.scrollRecycleView(uid);
                break;
            case "bilateral":
                setDataRefresh(true);
                mPresenter.getBilateralById(uid);
                mPresenter.scrollRecycleView(uid);
                break;
        }
    }
    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        switch (tag) {
            case "friends":
                setDataRefresh(true);
                mPresenter.getFriendById(uid);
                break;
            case "followers":
                setDataRefresh(true);
                mPresenter.getFollowerById(uid);
                break;
            case "bilateral":
                setDataRefresh(true);
                mPresenter.getBilateralById(uid);
                break;
        }
    }


    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return friend_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    public String getFgTag() {
        return tag;
    }
}
