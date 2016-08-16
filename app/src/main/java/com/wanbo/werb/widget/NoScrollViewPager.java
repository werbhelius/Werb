package com.wanbo.werb.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Werb on 2015/11/13.
 * 取消ViewPager的滑动事件
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //是否拦截事件,返回false表示不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //禁止滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
