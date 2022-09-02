package com.heyindi.tingshu.repository;

import com.heyindi.tingshu.data_object.AlbumDO;
import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AlbumRepo {
    @SneakyThrows
    public AlbumDO selectOneByUidAndAid(int uid, int aid) {
        String sql = "select aid, uid, title, cover, state from albums where uid = ? and aid = ?";

        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, aid);

                Log.println("执行 SQL:" + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    return new AlbumDO(rs.getInt("aid"), rs.getInt("uid"), rs.getString("title"), rs.getString("cover"), rs.getInt("state"));
                }
            }
        }
    }
}
