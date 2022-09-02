package com.heyindi.tingshu.service;

import com.heyindi.tingshu.data_object.AlbumDO;
import com.heyindi.tingshu.data_object.TrackDO;
import com.heyindi.tingshu.repository.AlbumRepo;
import com.heyindi.tingshu.repository.RelationRepo;
import com.heyindi.tingshu.repository.TrackRepo;
import com.heyindi.tingshu.view_object.BindView;
import com.heyindi.tingshu.view_object.UserVO;

import java.util.ArrayList;
import java.util.List;

public class AlbumService {
    private final AlbumRepo albumRepo = new AlbumRepo();
    private final RelationRepo relationRepo = new RelationRepo();
    private final TrackRepo trackRepo = new TrackRepo();

    public Object getBindInfo(UserVO currentUser, int aid) {
        AlbumDO albumDO = albumRepo.selectOneByUidAndAid(currentUser.uid, aid);
        if (albumDO == null) {
            throw new RuntimeException("aid 不存在或者不属于这个用户");
        }

        List<Integer> tidList = relationRepo.selectListByAid(aid);
        List<TrackDO> trackDOList;
        if (tidList.isEmpty()) {
            trackDOList = new ArrayList<>();
        } else {
            trackDOList = trackRepo.selectListByTidList(tidList);
        }

        return new BindView(currentUser, albumDO, trackDOList);
    }
}
