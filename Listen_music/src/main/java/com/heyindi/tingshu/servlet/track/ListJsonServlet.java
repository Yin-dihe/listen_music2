package com.heyindi.tingshu.servlet.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyindi.tingshu.service.TrackService;
import com.heyindi.tingshu.util.Log;
import com.heyindi.tingshu.view_object.StudioTrackListView;
import com.heyindi.tingshu.view_object.UserVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/studio/track/list.json")
public class ListJsonServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.println("访问 GET /studio/track/list.json");
        Log.println("QueryString: " + req.getQueryString());
        // 读取当前访问页面，需要考虑一些边界情况（没有传、不是数字）
        // 为了简单处理，就不做错误提示了，而是把异常值变成相对合理的合法值
        int page;
        String pageString = req.getParameter("page");
        if (pageString == null || pageString.trim().isEmpty()) {
            Log.println("请求参数中没有携带 页码 信息，默认成第 1 页");
            page = 1;
        } else {
            pageString = pageString.trim();
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException exc) {
                Log.println("请求参数中的 页码 信息，不是数字，默认成第 1 页");
                page = 1;
            }
        }
        Log.println("请求页数: " + page);

        // 1. 获取当前登录用户
        HttpSession session = req.getSession(false);
        UserVO currentUser = null;
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }

        // 2. 如果用户未登录 else 用户已登录
        StudioTrackListView resultView;
        if (currentUser == null) {
            Log.println("用户未登录");
            resultView = new StudioTrackListView();
            resultView.currentUser = null;  // 这步可以不写，因为默认值就是 null
            resultView.trackList = null;  // 这步可以不写，因为默认值就是 null
        } else {
            Log.println("用户已登录: " + currentUser);
            resultView = trackService.listOfUser(currentUser, page);
        }

        Log.println("准备 JSON 序列化的对象是: " + resultView);
        // 使用 Jackson 提供代码，将 list 对象，转化成（序列化）字符串类型的、JSON 格式
        String json = objectMapper.writeValueAsString(resultView);
        Log.println("JSON 序列化后的结果是: " + json);

        // 响应该字符串
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        Log.println("将 JSON 写入响应体中");
        writer.println(json);
    }
}
