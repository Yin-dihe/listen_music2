package com.heyindi.tingshu.servlet.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyindi.tingshu.service.AlbumService;
import com.heyindi.tingshu.view_object.UserVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/studio/album/bind.json")
public class BindJsonServlet extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        Object view = albumService.getBindInfo(currentUser, aid);

        String json = objectMapper.writeValueAsString(view);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
