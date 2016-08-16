package com.wanbo.werb.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * user of WeiBo
 */
@Table("user")public class User implements Serializable {

    @Column("id")private String id;
    @Column("idstr")private String idstr;
    @Column("screen_name")private String screen_name;
    @Column("name")private String name;
    @Column("location")private String location;
    @Column("description")private String description;
    @Column("profile_image_url") private String profile_image_url;
    @Column("profile_url")private String profile_url;
    @Column("gender")private String gender;
    @Column("followers_count")private int followers_count;
    @Column("friends_count")private int friends_count;
    @Column("statuses_count")private int statuses_count;
    @Column("favourites_count")private int favourites_count;
    @Column("verified")private boolean verified;
    @Column("avatar_large")private String avatar_large;
    @Column("avatar_hd")private String avatar_hd;
    @Column("cover_image_phone") private String cover_image_phone;

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public String getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getGender() {
        return gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    private String verified_reason;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", idstr='" + idstr + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", profile_url='" + profile_url + '\'' +
                ", gender='" + gender + '\'' +
                ", followers_count=" + followers_count +
                ", friends_count=" + friends_count +
                ", statuses_count=" + statuses_count +
                ", favourites_count=" + favourites_count +
                ", verified=" + verified +
                ", avatar_large='" + avatar_large + '\'' +
                ", avatar_hd='" + avatar_hd + '\'' +
                ", verified_reason='" + verified_reason + '\'' +
                '}';
    }
}
