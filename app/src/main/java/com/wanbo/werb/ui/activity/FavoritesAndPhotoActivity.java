package com.wanbo.werb.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wanbo.werb.R;
import com.wanbo.werb.bean.User;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.presenter.FAPPresenter;
import com.wanbo.werb.ui.view.IFAPView;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * favorites and photo
 */
public class FavoritesAndPhotoActivity extends MVPBaseActivity<IFAPView,FAPPresenter> implements IFAPView {

    private static final String TAG = "tag";
    private static final String USER = "user";

    private String tag;
    private User user;

    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    @Bind(R.id.f_and_p_list)
    RecyclerView f_and_p_list;

    @Override
    protected FAPPresenter createPresenter() {
        return new FAPPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_f_and_p;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        initView();
    }

    public static Intent newIntent(Context context, String tag, User user) {
        Intent intent = new Intent(context, FavoritesAndPhotoActivity.class);
        intent.putExtra(FavoritesAndPhotoActivity.TAG,tag);
        intent.putExtra(FavoritesAndPhotoActivity.USER,user);
        return intent;
    }

    private void parseIntent() {
        tag = getIntent().getStringExtra(TAG);
        user = (User) getIntent().getSerializableExtra(USER);
    }

    private void initView(){
        if(tag.equals("favorites")){
            mLayoutManager = new LinearLayoutManager(this);
            f_and_p_list.setLayoutManager(mLayoutManager);
            setDataRefresh(true);
            mPresenter.getFavorites();
            mPresenter.scrollRecycleView(user.getIdstr());
        }else if(tag.equals("photo")){
            gridLayoutManager = new GridLayoutManager(this,3);
            f_and_p_list.setLayoutManager(gridLayoutManager);
            setDataRefresh(true);
            mPresenter.getPhoto(user.getIdstr());
        }
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return f_and_p_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    public GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    @Override
    public String getTag() {
        return tag;
    }

}
