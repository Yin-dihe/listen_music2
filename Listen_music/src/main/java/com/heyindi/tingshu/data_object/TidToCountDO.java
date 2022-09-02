package com.heyindi.tingshu.data_object;

import lombok.Data;

@Data
public class TidToCountDO {
    public Integer tid;
    public Integer refCount;

    public TidToCountDO(int tid, int refCount) {
        this.tid = tid;
        this.refCount = refCount;
    }
}
