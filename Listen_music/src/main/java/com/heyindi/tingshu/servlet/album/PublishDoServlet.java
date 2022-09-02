package com.heyindi.tingshu.servlet.album;

import com.heyindi.tingshu.util.DBUtil;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/studio/album/publish.do")
public class PublishDoServlet extends HttpServlet {
    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: 验证当前登录用户是这个专辑的主人，才允许进行该操作
        String aid = req.getParameter("aid");

        try (Connection c = DBUtil.connection()) {
            String sql = "update albums set state = 2 where aid = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, aid);

                ps.executeUpdate();
            }
        }

        resp.sendRedirect("/studio/album/list.html");
    }
}
