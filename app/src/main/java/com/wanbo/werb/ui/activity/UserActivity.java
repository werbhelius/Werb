package com.wanbo.werb.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wanbo.werb.BuildConfig;
import com.wanbo.werb.MyApp;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.User;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.presenter.UserPresenter;
import com.wanbo.werb.ui.view.IUserView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Werb on 2016/7/31.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * About user when we click the icon
 * ps : 由于微博接口限制，只能获取当前授权用户的数据
 */
public class UserActivity extends MVPBaseActivity<IUserView,UserPresenter> implements IUserView {

    private static final String USER= "user";
    private User user;

    @Bind(R.id.tv_user_name)
    TextView userName;
    @Bind(R.id.tv_user_desc)
    TextView tv_user_desc;
    @Bind(R.id.tv_user_weibo_count)
    TextView tv_user_weibo_count;
    @Bind(R.id.tv_user_friends_count)
    TextView tv_user_friends_count;
    @Bind(R.id.tv_user_flowers_count)
    TextView tv_user_flowers_count;
    @Bind(R.id.iv_user_icon)
    CircleImageView iv_user_icon;
    @Bind(R.id.iv_user_bg)
    ImageView iv_user_bg;

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerView;

    @OnClick(R.id.iv_back) void back(){
        finish();
    }

    @Bind(R.id.tv_photo) TextView tv_photo;
    @OnClick(R.id.tv_photo) void startPhoto(){
        startActivity(FavoritesAndPhotoActivity.newIntent(this,"photo",user));
    }

    @Bind(R.id.tv_favorites) TextView tv_favorites;
    @OnClick(R.id.tv_favorites) void startFavorites(){
        startActivity(FavoritesAndPhotoActivity.newIntent(this,"favorites",user));
    }

    private LinearLayoutManager mLayoutManager;

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user;
    }

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.USER,user);
        return intent;
    }

    private void parseIntent() {
        user = (User) getIntent().getSerializableExtra(USER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        initUserInfo();

        if(BuildConfig.DEBUG){
            System.out.println("user_id:"+user.getIdstr());
        }

        //获取授权用户信息
        User userOAuth = getUserInfoFromDB();
        if(user.getIdstr().equals(userOAuth.getIdstr())) {
            setDataRefresh(true);
            mPresenter.getUserWeiBoTimeLine(this.user.getIdstr());
            mPresenter.scrollRecycleView(this.user.getIdstr());
        }else {
            Toast.makeText(this,"由于接口限制，无法获取未授权用户信息",Toast.LENGTH_SHORT).show();
            tv_favorites.setVisibility(View.GONE);
            tv_photo.setVisibility(View.GONE);
        }
    }

    private void initUserInfo(){
        userName.setText(user.getScreen_name());
        tv_user_desc.setText(user.getDescription());
        tv_user_weibo_count.setText("微博 " + user.getStatuses_count());
        tv_user_friends_count.setText("关注 " + user.getFriends_count());
        tv_user_flowers_count.setText("粉丝 " + user.getFollowers_count());
        Glide.with(this).load(user.getAvatar_hd()).centerCrop().into(iv_user_icon);
        Glide.with(this).load(user.getCover_image_phone()).centerCrop().into(iv_user_bg);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        tv_user_flowers_count.setOnClickListener(v -> startActivity(FriendShipsActivity.newIntent(UserActivity.this,"followers",user.getIdstr())));
        tv_user_friends_count.setOnClickListener(v -> startActivity(FriendShipsActivity.newIntent(UserActivity.this,"friends",user.getIdstr())));
    }

    @Override
    public Boolean isSetRefresh() {
        return true;
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getUserWeiBoTimeLine(user.getIdstr());
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    private User getUserInfoFromDB() {
        ArrayList<User> query = MyApp.mDb.query(User.class);
        if (query.size() > 0) {
            return query.get(0);
        } else {
            return null;
        }
    }
}
