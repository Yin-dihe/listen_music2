package com.heyindi.tingshu.servlet.album;

import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.view_object.UserVO;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/studio/album/create.do")
public class CreateDoServlet extends HttpServlet {
    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("未登录");
        }

        UserVO currentUser = (UserVO) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("未登录");
        }

        req.setCharacterEncoding("utf-8");
        String title = req.getParameter("title");
        String cover = req.getParameter("cover");
        int state = 1;  // 0: 已下线   1：未发布   2：已发布

        try (Connection c = DBUtil.connection()) {
            String sql = "insert into albums (uid, title, cover, state) values (?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, currentUser.uid);
                ps.setString(2, title);
                ps.setString(3, cover);
                ps.setInt(4, state);

                ps.executeUpdate();
            }
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        resp.getWriter().println("专辑创建成功");
    }
}
