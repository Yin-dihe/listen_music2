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

@WebServlet("/studio/album/candidate.json")
public class CandidateJsonServlet extends HttpServlet {
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
        public UserVO currentUser;
        public String aid;
        public List<TrackView> trackList = new ArrayList<>();
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aid = req.getParameter("aid");

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
        resultView.aid = aid;

        try (Connection c = DBUtil.connection()) {
            String sql = "select tid, title from tracks where uid = ? and tid not in (select tid from relations where aid = ?) order by tid desc";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, currentUser.uid);
                ps.setString(2, aid);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        resultView.trackList.add(new TrackView(rs));
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
