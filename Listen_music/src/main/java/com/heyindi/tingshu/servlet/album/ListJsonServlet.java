package com.heyindi.tingshu.servlet.album;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/studio/album/list.json")
public class ListJsonServlet extends HttpServlet {
    private static class ResultView {
        public UserVO currentUser;
        public List<AlbumView> albumList = new ArrayList<>();
    }
    private static class AlbumView {
        public Integer aid;
        public String title;
        public String cover;
        public String state;

        public AlbumView(int aid, String title, String cover, int state) {
            this.aid = aid;
            this.title = title;
            this.cover = cover;
            switch (state) {
                case 0: this.state = "已下线"; break;
                case 1: this.state = "未发布"; break;
                case 2: this.state = "已发布"; break;
                default: this.state = "未知"; break;
            }
        }
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取当前登录用户
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("未登录");
        }

        UserVO currentUser = (UserVO) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("未登录");
        }
        ResultView resultView = new ResultView();
        resultView.currentUser = currentUser;

        // 2. 执行 SQL
        // 3. 组合数据
        try (Connection c = DBUtil.connection()) {
            String sql = "select aid, title, cover, state from albums where uid = ? order by aid desc";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, currentUser.uid);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AlbumView view = new AlbumView(
                                rs.getInt("aid"),
                                rs.getString("title"),
                                rs.getString("cover"),
                                rs.getInt("state")
                        );

                        resultView.albumList.add(view);
                    }
                }
            }
        }
        // 4. JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultView);
        // 5. 响应
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
