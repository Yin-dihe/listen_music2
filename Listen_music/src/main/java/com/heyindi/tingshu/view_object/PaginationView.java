package com.heyindi.tingshu.view_object;

import lombok.Data;

@Data
public class PaginationView {
    public Integer countPerPage;
    public Integer currentPage;
    public Integer totalPage;
}
