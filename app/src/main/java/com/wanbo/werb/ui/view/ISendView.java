package com.wanbo.werb.ui.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Werb on 2016/8/3.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public interface ISendView {
    void permissionSetting();
    RecyclerView getPhotoGrid();
    void finishAndToast();
}
