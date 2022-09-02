package com.heyindi.tingshu.servlet.track;

import com.heyindi.tingshu.repository.TrackRepo;
import com.heyindi.tingshu.util.Log;
import com.heyindi.tingshu.view_object.UserVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet("/studio/track/upload.do")
// 要接收 enctype==multipart/formdata d  form 表单数据，必须用 @MultipartConfig 修饰
// 这里忘记修饰，就会出现 500 错误，错误原因就会提示没有修饰
@MultipartConfig
public class UploadDoServlet extends HttpServlet {
    private final TrackRepo trackRepo = new TrackRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 如果用户没有登录，则直接提示用户必须先登录
        UserVO userVO = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            userVO = (UserVO) session.getAttribute("currentUser");  // session 中的用户，是登录时候放进去的
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        if (userVO == null) {
            // 用户没有登录
            Log.println("用户未登录");
            writer.println("必须登录后才能使用这个功能");
            return;
        }

        // 说明用户已经登录了
        Log.println("登录用户为: " + userVO);

        // 2. 从请求中获取我们需要的信息
        req.setCharacterEncoding("utf-8");
        String title = req.getParameter("title");
        Log.println("获取到的 title 是 " + title);

        Part track = req.getPart("track");
        String type = track.getContentType();
        Log.println("获取到的 type 是 " + type);
        InputStream contentInputStream = track.getInputStream();    // 可以从这个 InputStream 中读取出来音频内容

        // 3. 将拿到的数据，插入到表中，由于做的操作比较简单，就不引入 service 了，直接使用 trackRepo 对象搞定
        trackRepo.insert(userVO.uid, title, type, contentInputStream);
        Log.println("上传成功");

        // 4. 响应插入成功
        writer.println("上传成功");
    }
}
