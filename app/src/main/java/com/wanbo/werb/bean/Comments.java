package com.wanbo.werb.bean;

import java.io.Serializable;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class Comments implements Serializable {

    private String created_at;
    private long id;
    private String idstr;
    private Status status;
    private User user;
    private String text;

    public String getCreated_at() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public Status getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "created_at='" + created_at + '\'' +
                ", id=" + id +
                ", idstr='" + idstr + '\'' +
                ", status=" + status +
                ", user=" + user +
                ", text='" + text + '\'' +
                '}';
    }
}
