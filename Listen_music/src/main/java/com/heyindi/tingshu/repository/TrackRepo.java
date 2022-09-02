package com.heyindi.tingshu.repository;

import com.heyindi.tingshu.data_object.TrackDO;
import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrackRepo {
    @SneakyThrows
    public void insert(int uid, String title, String type, InputStream content) {
        String sql = "insert into tracks (uid, title, type, content) values (?, ?, ?, ?)";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setString(2, title);
                ps.setString(3, type);
                ps.setBinaryStream(4, content); // 数据库表中的 longblob 就是一种二进制流 <-> BinaryStream

                Log.println("执行 SQL: " + ps.toString());
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public List<TrackDO> selectListByUidLimitOffset(int uid, int limit, int offset) {
        // 不要一次查太多的 content 字段，因为 content 比较大，一次查太多，会导致计算机负载很高
        List<TrackDO> list = new ArrayList<>();
        String sql = "select tid, uid, title, type from tracks where uid = ? order by tid desc limit ? offset ?";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, limit);
                ps.setInt(3, offset);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TrackDO trackDO = new TrackDO(
                                rs.getInt("tid"),
                                rs.getInt("uid"),
                                rs.getString("title"),
                                rs.getString("type")
                        );
                        list.add(trackDO);
                    }
                }
            }
        }
        return list;
    }

    @SneakyThrows
    public int selectCountByUid(int uid) {
        String sql = "select count(*) from tracks where uid = ?";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();  // 肯定是 1 条
                    return rs.getInt(1);
                }
            }
        }
    }

    @SneakyThrows
    public List<TrackDO> selectListByTidList(List<Integer> tidList) {   // [1, 2, 3, 4]
        // List<Integer> -> List<String>    // ["1", "2", "3", "4"]
        // String.join(", ", List<String>)  // "1, 2, 3, 4"
        String s = String.join(", ", tidList.stream().map(String::valueOf).collect(Collectors.toList()));

        String sql = String.format("select tid, uid, title, type from tracks where tid in (%s) order by tid", s);

        List<TrackDO> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                Log.println("执行 SQL: " + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new TrackDO(
                                rs.getInt("tid"),
                                rs.getInt("uid"),
                                rs.getString("title"),
                                rs.getString("type")
                        ));
                    }
                }
            }
        }

        return list;
    }
}
