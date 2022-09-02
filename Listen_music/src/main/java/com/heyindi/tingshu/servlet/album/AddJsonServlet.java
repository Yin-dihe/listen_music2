package com.heyindi.tingshu.servlet.album;

import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/studio/album/add.do")
public class AddJsonServlet extends HttpServlet {
    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("用户未登录");
        }

        UserVO currentUser = (UserVO) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        req.setCharacterEncoding("utf-8");
        int aid = Integer.parseInt(req.getParameter("aid"));
        String[] tidArray = req.getParameterValues("add-tid");
        List<Integer> tidList = Arrays.stream(tidArray).map(Integer::parseInt).collect(Collectors.toList());

        String sqlFormat = "insert into relations (aid, tid) values %s";
        String s = tidList.stream().map(tid -> String.format("(%d, %d)", aid, tid)).collect(Collectors.joining(", "));
        String sql = String.format(sqlFormat, s);
        Log.println("执行 SQL: " + sql);

        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        }


        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        resp.getWriter().println("绑定成功");
    }
}
