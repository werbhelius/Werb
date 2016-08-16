package com.wanbo.werb.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wanbo.werb.R;
import com.wanbo.werb.bean.Status;
import com.wanbo.werb.util.ScreenUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Werb on 2016/8/1.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Show Pic
 */
public class PicActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_URLS = "image_urls";
    public static final String POSITION = "position";
    private String mImageUrl;
    private List<Status.ThumbnailPic> mImageUrls = new ArrayList<>();
    private int position;
    PhotoViewAttacher mPhotoViewAttacher;
    @Bind(R.id.picture)
    ImageView picture;
    @Bind(R.id.vp_picture)
    ViewPager vp_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        parseIntent();
        if (mImageUrls == null) {
            if (mImageUrl.endsWith(".gif")) {
                Glide.with(this).load(mImageUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(picture);
            } else {
                int screenWidth = ScreenUtil.instance(this).getScreenWidth();
                int screenHeight = ScreenUtil.instance(this).getScreenHeight();
                picture.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenHeight));
                Glide.with(this).load(mImageUrl).into(picture);
            }
            mPhotoViewAttacher = new PhotoViewAttacher(picture);
        }else {
            picture.setVisibility(View.GONE);
            vp_picture.setVisibility(View.VISIBLE);
            vp_picture.setAdapter(new listPageAdapter());
            vp_picture.setCurrentItem(position);
        }
    }

    public static Intent newIntent(Context context, String url, List<Status.ThumbnailPic> urls,int position) {
        Intent intent = new Intent(context, PicActivity.class);
        intent.putExtra(PicActivity.IMAGE_URL, url);
        intent.putExtra(PicActivity.IMAGE_URLS, (Serializable) urls);
        intent.putExtra(PicActivity.POSITION, position);
        return intent;
    }

    /**
     * 得到Intent传递的数据
     */
    private void parseIntent() {
        String url = getIntent().getStringExtra(IMAGE_URL);
        position = getIntent().getIntExtra(POSITION,0);
        mImageUrls = (List<Status.ThumbnailPic>) getIntent().getSerializableExtra(IMAGE_URLS);
        if (url != null) {
            formatUrl(url);
        }else if(mImageUrls!=null) {
            System.out.println("--list--" + mImageUrls.size());
        }
        Toast.makeText(this, "loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(picture);
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void formatUrl(String url) {
        if (url.contains("thumbnail")) {
            mImageUrl = url.replace("thumbnail", "large");
            System.out.println("~thumbnail~~" + mImageUrl);
        } else {
            mImageUrl = url.replace("small", "large");
            System.out.println("~small~~" + mImageUrl);
        }
    }

    //通过ViewPager实现滑动的图片
    class listPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView pic = new ImageView(PicActivity.this);
            Glide.with(PicActivity.this).load(mImageUrls.get(position).getImage()).centerCrop().into(pic);
            container.addView(pic);
            mPhotoViewAttacher = new PhotoViewAttacher(pic);
            return pic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Glide.clear((View) object);
        }
    }
}
