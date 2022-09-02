package com.heyindi.tingshu.servlet.publish;

import com.heyindi.tingshu.util.DBUtil;
import com.heyindi.tingshu.util.Log;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/track")
public class TrackServlet extends HttpServlet {
    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tid = req.getParameter("tid");

        String type;
        InputStream content;
        try (Connection c = DBUtil.connection()) {
            String sql = "select type, content from tracks where tid = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, tid);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new RuntimeException("tid 对应的音频不存在，404");
                    }

                    type = rs.getString("type");
                    content = rs.getBinaryStream("content");
                }
            }
        }

        // 这里的响应不是文本，而是二进制，所以不需要设置字符集
        resp.setContentType(type);
        // 写二进制响应，不用 resp.getWriter() 而是用 resp.getOutputStream()
        ServletOutputStream os = resp.getOutputStream();
        // 把数据从 content(InputStream) 搬到 os(OutputStream)
        // IO 中学过的，利用一个桶(byte[]) 一点点搬，直到数据搬完

        byte[] buf = new byte[4096];
        while (true) {
            int n = content.read(buf);
            if (n == -1) {
                // 读到内容的最终了，退出循环
                break;
            }

            // 否则，将数据放入 os 中
            os.write(buf, 0, n);
        }

        os.flush();
        content.close();
    }
}
