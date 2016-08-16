package com.wanbo.werb.ui.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.wanbo.werb.widget.ClickCircleImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Werb on 2016/7/26.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * interface of mainActivity
 */
public interface IMainView {

    ImageView getUserBackGround();
    ClickCircleImageView getUserIcon();
    TextView getUserName();
}
