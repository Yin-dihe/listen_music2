package com.heyindi.tingshu.data_object;

import lombok.Data;

@Data
public class AlbumDO {
    public Integer aid;
    public Integer uid;
    public String title;
    public String cover;
    public Integer state;

    public AlbumDO(int aid, int uid, String title, String cover, int state) {
        this.aid = aid;
        this.uid = uid;
        this.title = title;
        this.cover = cover;
        this.state = state;
    }
}
