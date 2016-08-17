package com.wanbo.werb.bean;

import java.io.Serializable;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class Photo implements Serializable {

    private String pic_url;
    private String text;

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getText() {
        return text;
    }

}
