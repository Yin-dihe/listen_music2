package com.heyindi.tingshu.repository;

import com.heyindi.tingshu.data_object.TidToCountDO;
import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RelationRepo {
    @SneakyThrows
    public List<TidToCountDO> selectCountByTidListGroupByTidList(List<Integer> tidList) {
        String sqlFormat = "select tid, count(*) as ref_count from relations where tid in (%s) group by tid order by tid";
        StringBuilder sb = new StringBuilder();
        for (Integer tid : tidList) {
            sb.append(tid).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        String sql = String.format(sqlFormat, sb.toString());

        List<TidToCountDO> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TidToCountDO tidToCountDO = new TidToCountDO(
                                rs.getInt("tid"),
                                rs.getInt("ref_count")
                        );

                        list.add(tidToCountDO);
                    }
                }
            }
        }
        return list;
    }

    @SneakyThrows
    public List<Integer> selectListByAid(int aid) {
        String sql = "select tid from relations where aid = ? order by rid";
        List<Integer> tidList = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, aid);

                Log.println("执行 SQL: " + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        tidList.add(rs.getInt("tid"));
                    }
                }
            }
        }

        return tidList;
    }
}
