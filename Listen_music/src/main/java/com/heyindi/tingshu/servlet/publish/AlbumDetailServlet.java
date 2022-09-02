package com.heyindi.tingshu.servlet.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
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

@WebServlet("/album-detail.json")
public class AlbumDetailServlet extends HttpServlet {
    private static class TrackView {
        public Integer tid;
        public String title;

        @SneakyThrows
        public TrackView(ResultSet rs) {
            this.tid = rs.getInt("tid");
            this.title = rs.getString("title");
        }
    }

    private static class ResultView {
        public Integer aid;
        public String username;
        public String cover;
        public String title;

        public List<TrackView> trackList = new ArrayList<>();

        @SneakyThrows
        public void set(ResultSet rs) {
            this.aid = rs.getInt("aid");
            this.username = rs.getString("username");
            this.cover = rs.getString("cover");
            this.title = rs.getString("title");
        }
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aid = req.getParameter("aid");
        ResultView resultView = new ResultView();

        try (Connection c = DBUtil.connection()) {
            {
                String sql = "select aid, username, title, cover from albums a join users u on a.uid = u.uid where aid = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, aid);
                    Log.println("执行 SQL: " + ps);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new RuntimeException("aid 对应的专辑不存在, 404");
                        }

                        resultView.set(rs);
                    }
                }
            }

            List<String> tidList = new ArrayList<>();
            {
                String sql = "select tid from relations where aid = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, aid);

                    Log.println("执行 SQL: " + ps);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            tidList.add(rs.getString("tid"));
                        }
                    }
                }
            }

            if (tidList.isEmpty()) {
                throw new RuntimeException("这个专辑没有绑定任何的音频");
            }

            {
                String sqlFormat = "select tid, title from tracks where tid in (%s) order by tid desc";
                String s = String.join(", ", tidList);
                String sql = String.format(sqlFormat, s);

                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    Log.println("执行 SQL: " + ps);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            TrackView view = new TrackView(rs);
                            resultView.trackList.add(view);
                        }
                    }
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultView);
        Log.println("响应 JSON: " + json);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
