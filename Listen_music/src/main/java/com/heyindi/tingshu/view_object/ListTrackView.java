package com.heyindi.tingshu.view_object;

import com.heyindi.tingshu.data_object.TrackDO;
import lombok.Data;

// 这类的属性有哪些，叫什么名字，只和最终 JSON 长什么样有关，不需要考虑表里的东西
@Data
public class ListTrackView {
    public Integer tid;
    public String title;
    public Integer refCount;

    public ListTrackView(TrackDO trackDO, int refCount) {
        this.tid = trackDO.tid;
        this.title = trackDO.title;
        this.refCount = refCount;
    }
}
