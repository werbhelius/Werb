package com.wanbo.werb.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.presenter.MessageFgPresenter;
import com.wanbo.werb.ui.view.IMessageFgView;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/11.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * MessageFragment
 */
public class MessageFragment extends MVPBaseFragment<IMessageFgView,MessageFgPresenter> implements IMessageFgView {

    private static final String TAG = "tag";

    private String tag;

    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.message_list)
    RecyclerView message_list;

    @Override
    protected MessageFgPresenter createPresenter() {
        return new MessageFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
            return R.layout.fragment_message;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
    }

    public static MessageFragment newInstance(String tag) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        args.putString(MessageFragment.TAG, tag);
        MessageFragment fragment = new MessageFragment();
        //将Bundle设置为fragment的参数
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        message_list.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (tag) {
            case "at_weibo":
                setDataRefresh(true);
                mPresenter.getMessageAtWeibo();
                break;
            case "at_comment":
                setDataRefresh(true);
                mPresenter.getMentionComment();
                mPresenter.scrollRecycleView();
                break;
            case "get_comment":
                setDataRefresh(true);
                mPresenter.getMessageGetComment();
                mPresenter.scrollRecycleView();
                break;
            case "send_comment":
                setDataRefresh(true);
                mPresenter.getMessagesendComment();
                mPresenter.scrollRecycleView();
                break;
        }
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        switch (tag) {
            case "at_weibo":
                setDataRefresh(true);
                mPresenter.getMessageAtWeibo();
                break;
            case "at_comment":
                setDataRefresh(true);
                mPresenter.getMentionComment();
                break;
            case "get_comment":
                setDataRefresh(true);
                mPresenter.getMessageGetComment();
                break;
            case "send_comment":
                setDataRefresh(true);
                mPresenter.getMessagesendComment();
                break;
        }
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return message_list;
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
