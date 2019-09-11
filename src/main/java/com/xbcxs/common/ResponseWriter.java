package com.xbcxs.common;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * response封装
 *
 * @author xiaosh
 * @date 2019/9/5
 */
public class ResponseWriter {

    private ResponseWriter() {

    }

    public static void writer(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
