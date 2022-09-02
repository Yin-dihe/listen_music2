package com.heyindi.tingshu.view_object;

import com.heyindi.tingshu.data_object.UserDO;
import lombok.Data;

@Data   // 自动添加 getter/setter/toString/equals
// 由于这个类的对象是要准备传给前端的数据，所以千万不要把密码记录下来
public class UserVO {
    public Integer uid;
    public String username;

    public UserVO(UserDO userDO) {
        this.uid = userDO.uid;
        this.username = userDO.username;
    }
}
