package com.wanbo.werb.ui.presenter;

import com.wanbo.werb.api.WeiBoApi;
import com.wanbo.werb.api.WeiBoFactory;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Werb on 2016/7/25.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * Base of Presenter
 */
public abstract class BasePresenter<T> {

    protected Reference<T> mViewRef;

    public static final WeiBoApi weiBoApi = WeiBoFactory.getWeiBoApiSingleton();

    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }

    protected T getView(){
        return mViewRef.get();
    }

    public boolean isViewAttached(){
        return mViewRef != null&&mViewRef.get()!=null;
    }

    public void detachView(){
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
