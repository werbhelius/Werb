package com.wanbo.werb.ui.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public interface IFAPView {
    void setDataRefresh(Boolean refresh);
    RecyclerView getRecyclerView();
    LinearLayoutManager getLayoutManager();
    GridLayoutManager getGridLayoutManager();
    String getTag();
}
