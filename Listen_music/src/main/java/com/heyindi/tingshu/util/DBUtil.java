package com.heyindi.tingshu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

public class DBUtil {
    private static final DataSource dataSource;

    static {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/java19_tingshu?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("123456");
        dataSource = mysqlDataSource;
    }

    @SneakyThrows
    public static Connection connection() {
        return dataSource.getConnection();
    }
}
