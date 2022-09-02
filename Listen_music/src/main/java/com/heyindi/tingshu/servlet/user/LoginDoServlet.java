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
import java.io.PrintWriter;

@WebServlet("/studio/user/login.do")
public class LoginDoServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserVO userVO = userService.login(username, password);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        if (userVO == null) {
            writer.println("登录失败");
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", userVO);
            writer.println("登录成功");
        }
    }
}
