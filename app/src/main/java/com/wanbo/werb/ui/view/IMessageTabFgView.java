package com.wanbo.werb.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Werb on 2016/8/13.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public interface IMessageTabFgView {

    void setDataRefresh(Boolean refresh);
    RecyclerView getRecyclerView();
    LinearLayoutManager getLayoutManager();
}
