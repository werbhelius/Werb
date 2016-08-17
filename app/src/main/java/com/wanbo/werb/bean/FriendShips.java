package com.wanbo.werb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Werb on 2016/8/17.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class FriendShips implements Serializable {

    private String next_cursor;
    private String previous_cursor;
    private String total_number;
    private List<User> users;

    public String getNext_cursor() {
        return next_cursor;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public String getTotal_number() {
        return total_number;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "FriendShips{" +
                "next_cursor='" + next_cursor + '\'' +
                ", previous_cursor='" + previous_cursor + '\'' +
                ", total_number='" + total_number + '\'' +
                ", users=" + users +
                '}';
    }
}
