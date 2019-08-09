package com.xbcxs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiaosh on 2019/8/5.
 */
public class ResponseWriter {

    Logger log = LoggerFactory.getLogger(ResponseWriter.class);

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
