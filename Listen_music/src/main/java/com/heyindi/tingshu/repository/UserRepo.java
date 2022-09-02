package com.heyindi.tingshu.repository;

import com.heyindi.tingshu.data_object.UserDO;
import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// 负责直接使用 SQL 对数据库的增删查改操作
// 操作的是 users 表
// 查出来的数据，以 data_object.UserDO 来作为具体的表示
public class UserRepo {
    @SneakyThrows   // lombok，可以让我们不添加 throws 也可以正常抛出受查异常
    public void insert(UserDO userDO) {
        // 这里进行 SQL 操作
        String sql = "insert into users (username, password) values (?, ?)";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, userDO.username);
                ps.setString(2, userDO.password);

                Log.println("执行 SQL: " + ps.toString());
                ps.executeUpdate();

                // 得到自增 id 作为 uid
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    userDO.uid = rs.getInt(1);
                }
            }
        }
    }

    @SneakyThrows
    public UserDO selectOneByUsername(String username) {
        String sql = "select uid, username, password from users where username = ?";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, username);

                Log.println("执行 SQL: " + ps.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }


                    return new UserDO(
                            rs.getInt("uid"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        }
    }
}
