package com.wanbo.werb.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * every weibo info
 */
public class Status implements Serializable{

    private String created_at;
    private long id;
    private String idstr;
    private String text;
    private String source;
    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;
    private String reposts_count;
    private String comments_count;
    private String attitudes_count;
    private Status retweeted_status;
    private ArrayList<ThumbnailPic> pic_urls;

    private User user;

    public String getCreated_at() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public String getAttitudes_count() {
        return attitudes_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public String getReposts_count() {
        return reposts_count;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public String getSource() {
        return source;
    }

    public String getText() {
        return text;
    }

    public ArrayList<ThumbnailPic> getPic_urls() {
        return pic_urls;
    }

    public User getUser() {
        return user;
    }


    public class ThumbnailPic implements Serializable{
        private String thumbnail_pic;

        public String getLargeImg(){
            return thumbnail_pic.replace("thumbnail", "large");
        }

        public String getSmallImg(){
            return thumbnail_pic.replace("thumbnail", "small");
        }

        public String getThumbnail_pic() {
            return thumbnail_pic;
        }

        public String getImage(){
            if (getThumbnail_pic().contains("thumbnail")) {
                return getThumbnail_pic().replace("thumbnail", "large");
            } else {
                return getThumbnail_pic().replace("small", "large");
            }
        }
    }



    @Override
    public String toString() {
        return "Status{" +
                "created_at='" + created_at + '\'' +
                ", id=" + id +
                ", idstr='" + idstr + '\'' +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", thumbnail_pic='" + thumbnail_pic + '\'' +
                ", bmiddle_pic='" + bmiddle_pic + '\'' +
                ", original_pic='" + original_pic + '\'' +
                ", reposts_count='" + reposts_count + '\'' +
                ", comments_count='" + comments_count + '\'' +
                ", attitudes_count='" + attitudes_count + '\'' +
                ", retweeted_status=" + retweeted_status +
                ", pic_urls=" + pic_urls +
                ", user=" + user +
                '}';
    }
}
