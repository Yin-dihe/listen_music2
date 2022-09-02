package com.heyindi.tingshu.servlet.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/album-list.json")
public class AlbumListServlet extends HttpServlet {
    private static class AlbumView {
        public Integer aid;
        public String username;
        public String title;
        public String cover;

        @SneakyThrows
        public AlbumView(ResultSet rs) {
            this.aid = rs.getInt("aid");
            this.username = rs.getString("username");
            this.title = rs.getString("title");
            this.cover = rs.getString("cover");
        }
    }
    private static class ResultView {
        public List<AlbumView> albumList = new ArrayList<>();
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultView resultView = new ResultView();
        try (Connection c = DBUtil.connection()) {
            String sql = "select aid, username, title, cover from albums a join users u on a.uid = u.uid where state = 2 order by aid desc";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AlbumView view = new AlbumView(rs);
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
