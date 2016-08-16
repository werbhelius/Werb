package com.wanbo.werb.bean;

/**
 * Created by Werb on 2016/8/3.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class PostComments {

    private String access_token;
    private String comment;
    private String id;

    public PostComments(String access_token, String comment, String id) {
        this.access_token = access_token;
        this.comment = comment;
        this.id = id;
    }
}
