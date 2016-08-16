package com.wanbo.werb.util;


/**
 * Created by Werb on 2016/7/13.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * RxBus异步响应事件类型
 */
public class RxEvents {

    /**
     * 返回刷新点击事件
     */
    public static class UpRefreshClick{}

    /**
     * 微博点赞
     */
    public static class WeiBoSetLike{

        private boolean isLike;
        private String id;

        public WeiBoSetLike(boolean isLike, String id) {
            this.isLike = isLike;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public boolean isLike() {
            return isLike;
        }
    }

}
