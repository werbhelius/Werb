package com.wanbo.werb.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanbo.werb.R;
import com.wanbo.werb.ui.presenter.TabPresenter;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.view.ITabView;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * 由于接口限制，只能获取评论数据，转发与点赞数据原理相同，设置对应Tag不同请求就可以
 */
public class TabFragment extends MVPBaseFragment<ITabView,TabPresenter> implements ITabView {

    private static final String WERBO_ID = "weibo_id";
    private static final String FG_TAg = "fg_tag";

    private LinearLayoutManager mLayoutManager;

    private String tag;
    private String weiBo_id;

    @Bind(R.id.fg_tab_list)
    RecyclerView fg_tab_list;

    public static TabFragment newInstance(String pid, String tag) {
        //通过Bundle保存数据
        Bundle args = new Bundle();
        args.putString(TabFragment.WERBO_ID, pid);
        args.putString(TabFragment.FG_TAg, tag);
        TabFragment fragment = new TabFragment();
        //将Bundle设置为fragment的参数
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected TabPresenter createPresenter() {
        return new TabPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weiBo_id = getArguments().getString(WERBO_ID);
        tag = getArguments().getString(FG_TAg);
        System.out.println("--id--tag--"+weiBo_id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getComments();
        mPresenter.scrollRecycleView();
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        fg_tab_list.setLayoutManager(mLayoutManager);
    }

    @Override
    public Boolean isSetRefresh() {
        return false;
    }

    @Override
    public String getWeiBoId() {
        return weiBo_id;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return fg_tab_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }
}
