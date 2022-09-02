package com.heyindi.tingshu.servlet.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyindi.tingshu.repository.TrackRepo;
import com.heyindi.tingshu.util.Log;
import com.heyindi.tingshu.view_object.UserVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/studio/track/record.json")
public class RecordJsonServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TrackRepo trackRepo = new TrackRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String title = req.getParameter("title");
        String type = req.getHeader("Content-Type");
        Log.println("title: " + title);
        Log.println("type: " + type);

        UserVO currentUser = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }


        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        if (currentUser == null) {
            Log.println("用户未登录");
            HashMap<String, Boolean> result = new HashMap<>();
            result.put("result", false);
            String json = objectMapper.writeValueAsString(result);
            Log.println(json);
            writer.println(json);
            return;
        }

        trackRepo.insert(currentUser.uid, title, type, req.getInputStream());
        Log.println("将数据保存到数据库表中");

        HashMap<String, Boolean> result = new HashMap<>();
        result.put("result", true);
        String json = objectMapper.writeValueAsString(result);
        Log.println(json);
        writer.println(json);

        // 完整的音频数据放在 InputStream（请求体）
//        ServletInputStream is = req.getInputStream();
//        try (OutputStream os = new FileOutputStream("D:\\upload" + title + ".ogg")) {
//            byte[] buf = new byte[4096];
//            while (true) {
//                int n = is.read(buf);
//                Log.println("读到了: " + n);
//                if (n == -1) {
//                    break;
//                }
//                os.write(buf, 0, n);
//            }
//            os.flush();
//        }
    }
}
