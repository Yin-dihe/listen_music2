package com.heyindi.tingshu.view_object;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class StudioTrackListView {
    public UserVO currentUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public PaginationView pagination;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // 只有这个属性的值是非 null 的时候，才会出现在 JSON
    public List<ListTrackView> trackList;
}
