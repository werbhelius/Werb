package com.wanbo.werb.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wanbo.werb.ui.base.MVPBaseFragment;

import java.util.List;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class ViewPagerFgAdapter extends FragmentPagerAdapter {

    private String tag;

    private List<MVPBaseFragment> fragmentList;


    public ViewPagerFgAdapter(FragmentManager supportFragmentManager, List<MVPBaseFragment> fragmentList, String tag) {
        super(supportFragmentManager);
        this.fragmentList = fragmentList;
        this.tag = tag;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        if (fragmentList != null) {
            return fragmentList.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tag.equals("WeiBo_Detail")) {
            switch (position) {
                case 0:
                    return "转发";
                case 1:
                    return "评论";
                case 2:
                    return "赞";
            }
        } else if (tag.equals("Message")) {
            switch (position) {
                case 0:
                    return "@我";
                case 1:
                    return "@我的评论";
                case 2:
                    return "收到的评论";
                case 3:
                    return "发出的评论";
            }
        } else if (tag.equals("FriendShips")){
            switch (position) {
                case 0:
                    return "关注";
                case 1:
                    return "粉丝";
                case 2:
                    return "互粉";
            }
        }
        return null;
    }
}
