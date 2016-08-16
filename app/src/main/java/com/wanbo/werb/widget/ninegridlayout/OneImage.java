package com.wanbo.werb.widget.ninegridlayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wanbo.werb.ui.activity.PicActivity;
import com.wanbo.werb.util.ScreenUtil;

/**
 * Created by Werb on 2016/7/12.
 * Email：1025004680@qq.com
 * 单图显示 imageView
 */
public class OneImage extends ImageView implements View.OnClickListener {

    private String url;
    private boolean isAttachedToWindow;

    public OneImage(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public OneImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public OneImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Glide.clear(this);
    }

    public void setImageUrl(String url) {

        int width = ScreenUtil.instance(getContext()).getScreenWidth();
        int height = width/3;

        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
                Glide.with(getContext().getApplicationContext()).load(url).override((width/2), height).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
            }
        }
    }


    //打开PicActivity
    private void startPictureActivity(OneImage image) {
        Intent intent = PicActivity.newIntent(getContext(), image.url,null,0);
        //异常处理
        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        startPictureActivity(this);
    }
}
