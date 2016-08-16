package com.wanbo.werb.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public interface ITabView {

    String getWeiBoId();
    RecyclerView getRecyclerView();
    LinearLayoutManager getLayoutManager();
}
