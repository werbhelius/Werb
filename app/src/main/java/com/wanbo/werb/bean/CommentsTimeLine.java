package com.wanbo.werb.bean;

import java.util.List;

/**
 * Created by Werb on 2016/8/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class CommentsTimeLine {

    private List<Comments> comments;
    private String max_id;
    private String since_id;

    public List<Comments> getComments() {
        return comments;
    }

    public String getMax_id() {
        return max_id;
    }

    public String getSince_id() {
        return since_id;
    }

    @Override
    public String toString() {
        return "CommentsTimeLine{" +
                "comments=" + comments +
                ", max_id='" + max_id + '\'' +
                ", since_id='" + since_id + '\'' +
                '}';
    }
}
