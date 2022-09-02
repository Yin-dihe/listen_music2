package com.heyindi.tingshu.data_object;

import lombok.Data;

// 严格按照数据库表来写
@Data
public class TrackDO {
    public Integer tid;
    public Integer uid;
    public String title;
    public String type;
    public byte[] content;

    public TrackDO(int tid, int uid, String title, String type) {
        this.tid = tid;
        this.uid = uid;
        this.title = title;
        this.type = type;
    }
}
