package example.controller;

import com.alibaba.fastjson.JSONObject;
import example.utils.HashData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@WebServlet(name = "debug", value = "/debug")
public class debug extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File directory = new File("./debug/");//设定为当前文件夹
        response.getWriter().print("cwd:" + directory.getCanonicalPath() + "\npwd:" + directory.getAbsolutePath());
    }
}