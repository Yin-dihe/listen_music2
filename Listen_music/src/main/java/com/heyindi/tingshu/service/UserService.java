package com.heyindi.tingshu.service;

import com.heyindi.tingshu.data_object.UserDO;
import com.heyindi.tingshu.repository.UserRepo;
import com.heyindi.tingshu.view_object.UserVO;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserRepo userRepo = new UserRepo();

    public UserVO register(String username, String password) {
        // 1. 先对密码做好”加密“，保证数据库中保存的密码不是明文密码
        String salt = BCrypt.gensalt(); // 生成随机数据（盐值）
        password = BCrypt.hashpw(password, salt);

        // 2. 完成数据库表的 insert 操作
        UserDO userDO = new UserDO(username, password);
        userRepo.insert(userDO);

        // 3. 将数据库中直接得到的数据转换成我们的表现形式
        return new UserVO(userDO);
    }

    public UserVO login(String username, String password) {
        // 1. 先根据用户名从表中查询出数据，用 UserDO 对象表示
        UserDO userDO = userRepo.selectOneByUsername(username);
        if (userDO == null) {
            // 说明用户输入的用户名就有问题
            return null;
        }

        // 2. 检查密码是否真正正确
        // plaintext : 明文密码 —— 用户刚才传入的密码
        // hashed : 经过 hash 后的密码 —— 从表中查出来的密码
        if (BCrypt.checkpw(password, userDO.password)) {
            // 返回 true，代表匹配，说明用户输入的密码大概率没问题
            return new UserVO(userDO);
        } else {
            // 代表用户输入的密码肯定有问题
            return null;
        }
    }
}
