package com.wanbo.werb.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;

import java.util.List;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * FriendsTimeLine of WeiBo,now we can many things from this.
 */
@Table("friends") public class FriendsTimeLine {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id") private long id;
    @Column("since_id") private String since_id;
    @Column("max_id") private String max_id;
    @Mapping(Relation.OneToMany) private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public String getSince_id() {
        return since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    @Override
    public String toString() {
        return "FriendsTimeLine{" +
                "since_id='" + since_id + '\'' +
                ", max_id='" + max_id + '\'' +
                ", statuses=" + statuses +
                '}';
    }
}
