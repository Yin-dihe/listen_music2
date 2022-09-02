package com.heyindi.tingshu.service;

import com.heyindi.tingshu.data_object.TidToCountDO;
import com.heyindi.tingshu.data_object.TrackDO;
import com.heyindi.tingshu.repository.RelationRepo;
import com.heyindi.tingshu.repository.TrackRepo;
import com.heyindi.tingshu.util.Log;
import com.heyindi.tingshu.view_object.ListTrackView;
import com.heyindi.tingshu.view_object.PaginationView;
import com.heyindi.tingshu.view_object.StudioTrackListView;
import com.heyindi.tingshu.view_object.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackService {
    private final TrackRepo trackRepo = new TrackRepo();
    private final RelationRepo relationRepo = new RelationRepo();

    public StudioTrackListView listOfUser(UserVO userVO, int page) {
        StudioTrackListView resultView = new StudioTrackListView();
        resultView.currentUser = userVO;

        // 0. 规定每页共有 5 条
        int countPerPage = 5;
        Log.println("每页个数: " + countPerPage);

        int count = trackRepo.selectCountByUid(userVO.uid);
        Log.println("属于当前用户的音频共有 " + count + " 个");
        if (count == 0) {
            resultView.pagination = new PaginationView();
            resultView.pagination.countPerPage = countPerPage;
            resultView.pagination.currentPage = 1;
            resultView.pagination.totalPage = 0;
            resultView.trackList = new ArrayList<>();
            return resultView;
        }

        int totalPage = count / countPerPage;
        if (count % countPerPage > 0) {
            // 向上取整
            totalPage++;
        }
        Log.println("共有页数: " + totalPage);

        // 针对页面的超出范围的值，同样调整成相对合理的合法值，这样就不用大量抛出异常或者错误提示了
        if (page < 1) {
            Log.println("页面信息小于最小值，调整为 1");
            page = 1;
        }
        if (page > totalPage) {
            Log.println("页面信息大于最大值，调整为最大值");
            page = totalPage;
        }
        int offset = (page - 1) * countPerPage;

        // 1. 从数据库表中查询即可
        List<TrackDO> list = trackRepo.selectListByUidLimitOffset(userVO.uid, countPerPage, offset);
        Log.println("从数据库表中查询到的结果: " + list);
        if (!list.isEmpty()) {
            Log.println("结果不为空，所以，需要继续查询关联数量");
            // 2. 根据结果，查询每个 track 对应的 refCount（引用次数）
            // 2.1 先从 list 得到每一个 tid
            List<Integer> tidList = new ArrayList<>();
            for (TrackDO trackDO : list) {
                tidList.add(trackDO.tid);
            }
            // 2.2 拿着 tidList 从 relations 表中得到需要的结果 Map<tid, refCount>
            Map<Integer, Integer> tidToRefCountMap = new HashMap<>();
            List<TidToCountDO> tidToCountDOList = relationRepo.selectCountByTidListGroupByTidList(tidList);
            Log.println("查询 relations 表得到的结果: " + tidToCountDOList);
            for (TidToCountDO tidToCountDO : tidToCountDOList) {
                tidToRefCountMap.put(tidToCountDO.tid, tidToCountDO.refCount);
            }
            Log.println("tid to ref count map: " + tidToRefCountMap);

            // 3. 将 TrackDo -> ListTrackView
            List<ListTrackView> resultList = new ArrayList<>();
            for (TrackDO trackDO : list) {
                ListTrackView view = new ListTrackView(trackDO, tidToRefCountMap.getOrDefault(trackDO.tid, 0));
                resultList.add(view);
            }
            Log.println("转换成准备输出的结果: " + resultList);
            resultView.trackList = resultList;
        } else {
            Log.println("结果为空，所以，关联数量就不需要管了");
            resultView.trackList = new ArrayList<>();
        }

        // 3. 查询出页面信息
        PaginationView paginationView = new PaginationView();
        paginationView.countPerPage = countPerPage;
        paginationView.currentPage = page;
        paginationView.totalPage = totalPage;
        resultView.pagination = paginationView;

        return resultView;
    }
}
