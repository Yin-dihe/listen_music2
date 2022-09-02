package com.heyindi.tingshu.servlet.user;

import com.heyindi.tingshu.service.UserService;
import com.heyindi.tingshu.view_object.UserVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Web 中的一个动态资源
@WebServlet("/studio/user/register.do")
public class RegisterDoServlet extends HttpServlet {
    // 由于表示过程的对象，一般都是单例的，定义成属性，可以减少对象的创建，相对来说性能能好些
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 读取用户名 + 密码
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // TODO: 输入合法性校验

        // 2. 完成注册操作（密码加密 + insert 表，所以放到 service 中处理）
        // 由于这个用户对象要放到 session 中，属于表现的对象，所以使用 view_object
        UserVO userVO = userService.register(username, password);

        // 3. 注册后直接进行登录（将用户对象放入 session 中）
        HttpSession session = req.getSession();
        session.setAttribute("currentUser", userVO);

        // 4. 响应注册成功
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        resp.getWriter().println("注册成功");
    }
}
