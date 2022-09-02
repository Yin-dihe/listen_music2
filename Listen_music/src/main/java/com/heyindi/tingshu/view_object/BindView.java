package com.heyindi.tingshu.view_object;

import com.heyindi.tingshu.data_object.AlbumDO;
import com.heyindi.tingshu.data_object.TrackDO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BindView {

    public static class TrackView {
        public Integer tid;
        public String title;

        public TrackView(Integer tid, String title) {
            this.tid = tid;
            this.title = title;
        }
    }

    public UserVO currentUser;
    public Integer aid;
    public String title;
    public List<TrackView> trackList;

    public BindView(UserVO currentUser, AlbumDO albumDO, List<TrackDO> trackDOList) {
        this.currentUser = currentUser;
        this.aid = albumDO.aid;
        this.title = albumDO.title;

//        this.trackList = new ArrayList<>();
//        for (TrackDO trackDO : trackDOList) {
//            TrackView view = new TrackView(trackDO.tid, trackDO.title);
//            trackList.add(view);
//        }

//        {
//            Stream<TrackDO> stream = trackDOList.stream();
//            Stream<TrackView> stream2 = (Stream<TrackView>) stream.map(new Function<TrackDO, TrackView>() {
//                @Override
//                public TrackView apply(TrackDO trackDO) {
//                    return new TrackView(trackDO.tid, trackDO.title);
//                }
//            });
//            List<TrackDO> collect = stream.collect(Collectors.toList());
//        }
        {
            this.trackList = trackDOList.stream().map(d -> new TrackView(d.tid, d.title)).collect(Collectors.toList());
        }
    }
}
