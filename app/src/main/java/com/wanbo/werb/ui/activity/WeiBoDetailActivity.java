package com.wanbo.werb.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.ui.base.MVPBaseFragment;
import com.wanbo.werb.ui.presenter.WeiBoDetailPresenter;
import com.wanbo.werb.ui.adapter.ViewPagerFgAdapter;
import com.wanbo.werb.ui.base.MVPBaseActivity;
import com.wanbo.werb.ui.fragment.TabFragment;
import com.wanbo.werb.ui.view.IWeiBoDetailView;
import com.wanbo.werb.util.DataUtil;
import com.wanbo.werb.util.StringUtil;
import com.wanbo.werb.widget.ClickCircleImageView;
import com.wanbo.werb.widget.ninegridlayout.NineGridlayout;
import com.wanbo.werb.widget.ninegridlayout.OneImage;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/1.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * WeiBo Detail
 */
public class WeiBoDetailActivity extends MVPBaseActivity<IWeiBoDetailView,WeiBoDetailPresenter> implements IWeiBoDetailView, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    public static final String WEIBO_STATUS = "weibo_status";
    public Status mStatus;
    public String weibo_id;
    private List<MVPBaseFragment> fragmentList;

    //悬浮按钮
    @Bind(R.id.fab_layout)
    RapidFloatingActionLayout fab_layout;
    @Bind(R.id.fab_button_group)
    RapidFloatingActionButton fab_button_group;
    private RapidFloatingActionHelper rfabHelper;

    //原文
    @Bind(R.id.tv_weibo_userName)
    TextView tv_weibo_userName;
    @Bind(R.id.tv_weibo_create_time)
    TextView tv_weibo_create_time;
    @Bind(R.id.tv_weibo_text)
    TextView tv_weibo_text;
    @Bind(R.id.iv_weibo_icon)
    ClickCircleImageView iv_weibo_icon;
    @Bind(R.id.iv_one_image)
    OneImage iv_one_image;
    @Bind(R.id.iv_nine_grid_layout)
    NineGridlayout iv_nine_grid_layout;

    //转发
    @Bind(R.id.layout_weibo_zhuanfa)
    LinearLayout layout_weibo_zhuanfa;
    @Bind(R.id.tv_weibo_zhuanfa_userName)
    TextView tv_weibo_zhuanfa_userName;
    @Bind(R.id.tv_weibo_zhuanfa_text)
    TextView tv_weibo_zhuanfa_text;
    @Bind(R.id.iv_z_one_image)
    OneImage iv_z_one_image;
    @Bind(R.id.iv_z_nine_grid_layout)
    NineGridlayout iv_z_nine_grid_layout;
    @Bind(R.id.tv_weibo_zhuanfa_reposts_count)
    TextView tv_weibo_zhuanfa_reposts_count;
    @Bind(R.id.tv_weibo_zhuanfa_comments_count)
    TextView tv_weibo_zhuanfa_comments_count;

    @Override
    protected WeiBoDetailPresenter createPresenter() {
        return new WeiBoDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_weibo_detail;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        initViewWithStatus(mStatus);
        initTabView();
        initFab();
    }

    public static Intent newIntent(Context context, Status status) {
        Intent intent = new Intent(context, WeiBoDetailActivity.class);
        intent.putExtra(WeiBoDetailActivity.WEIBO_STATUS,status);
        return intent;
    }

    /**
     * 得到Intent传递的数据
     */
    private void parseIntent() {
        mStatus = (Status) getIntent().getSerializableExtra(WEIBO_STATUS);
        weibo_id = mStatus.getIdstr();
        System.out.println("---detail--id-"+weibo_id);
    }

    //初始化Tab滑动
    public void initTabView(){

        fragmentList = new ArrayList<>();
        fragmentList.add(TabFragment.newInstance(weibo_id,"zhuanfa"));
        fragmentList.add(TabFragment.newInstance(weibo_id,"comments"));
        fragmentList.add(TabFragment.newInstance(weibo_id,"comments"));

        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        ViewPager mViewPager= (ViewPager) findViewById(R.id.detail_viewPager);

        mViewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(),fragmentList,"WeiBo_Detail"));//给ViewPager设置适配器
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来

    }

    /**
     * 初始化详情微博界面的数据
     * @param status 数据
     */
    private void initViewWithStatus(Status status){
        tv_weibo_userName.setText(status.getUser().getScreen_name());
        tv_weibo_create_time.setText(DataUtil.showTime(status.getCreated_at()));
        tv_weibo_text.setMovementMethod(LinkMovementMethod.getInstance());
        tv_weibo_text.setText(StringUtil.getWeiBoText(this,status.getText()));
        //头像
        Glide.clear(iv_weibo_icon);
        iv_weibo_icon.setUserImage(status.getUser());
        if (status.getRetweeted_status() == null) {
            layout_weibo_zhuanfa.setVisibility(View.GONE);
            setWeiBoImg(status.getPic_urls(), iv_one_image, iv_nine_grid_layout);
        } else {
            layout_weibo_zhuanfa.setVisibility(View.VISIBLE);
            iv_nine_grid_layout.setVisibility(View.GONE);
            iv_one_image.setVisibility(View.GONE);
            tv_weibo_zhuanfa_reposts_count.setText("转发:(" + status.getRetweeted_status().getReposts_count() + ")");
            tv_weibo_zhuanfa_comments_count.setText("评论:(" + status.getRetweeted_status().getComments_count() + ")");
            // zhuanfa userName
            if (status.getRetweeted_status().getUser() != null && !status.getRetweeted_status().getUser().equals("")) {
                tv_weibo_zhuanfa_userName.setMovementMethod(LinkMovementMethod.getInstance());
                tv_weibo_zhuanfa_userName.setText(StringUtil.getWeiBoText(this,"@" + status.getRetweeted_status().getUser().getScreen_name()));
            }
            // zhuanfa text
            tv_weibo_zhuanfa_text.setMovementMethod(LinkMovementMethod.getInstance());
            tv_weibo_zhuanfa_text.setText(StringUtil.getWeiBoText(this,status.getRetweeted_status().getText()));
            // img
            setWeiBoImg(status.getRetweeted_status().getPic_urls(), iv_z_one_image, iv_z_nine_grid_layout);
        }
    }

    /**
     * 初始化 Fab
     */
    private void initFab() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("转发")
                .setResId(R.drawable.skip_16px)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("评论")
                .setResId(R.drawable.comment_16px)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                fab_layout,
                fab_button_group,
                rfaContent
        ).build();

    }

    //设置图片
    private void setWeiBoImg(ArrayList<Status.ThumbnailPic> pic_urls, OneImage oneImage, NineGridlayout nineGridlayout) {
        if (pic_urls != null) {
            if (pic_urls.size() == 1) {
                nineGridlayout.setVisibility(View.GONE);
                oneImage.setVisibility(View.VISIBLE);
                oneImage.setImageUrl(pic_urls.get(0).getSmallImg());
            } else {
                nineGridlayout.setVisibility(View.VISIBLE);
                oneImage.setVisibility(View.GONE);
                nineGridlayout.setImagesData(pic_urls);

            }
        } else {
            nineGridlayout.setVisibility(View.GONE);
            oneImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {

    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if(i==1){
            startActivity(CommentAndRepostActivity.newIntent(WeiBoDetailActivity.this,mStatus,"回复微博",null));
        }else {
            startActivity(CommentAndRepostActivity.newIntent(WeiBoDetailActivity.this,mStatus,"转发微博",null));
        }
    }
}
