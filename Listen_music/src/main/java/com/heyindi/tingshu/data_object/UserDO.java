package com.heyindi.tingshu.data_object;

import lombok.Data;

@Data
// 由于这里是数据库操作的，所以这里的属性名称最好完全和数据库的字段名称一样，这样出起来方便
// users 表
public class UserDO {
    public Integer uid;
    public String username;
    public String password;

    public UserDO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDO(int uid, String username, String password) {
        this.uid = uid;
        this.username = username;
        this.password = password;
    }
}