package example.controller;

import com.alibaba.fastjson.JSONObject;
import example.utils.HashData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@WebServlet(name = "BackendServlet", value = "/BackendServlet")
public class BackendServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //算法端脑电专注度数据
//        String res = HashData.getHashValue("0x04","ss");
//        assert res != null;
//        String[] ret = res.split("-");
//        JSONObject js = new JSONObject();
//        js.put("concentration",ret[0]);
//        js.put("brainWaves",ret[1]);
//        PrintWriter out = response.getWriter();
//        out.print(js);

        //模拟
        JSONObject js = new JSONObject();
        int data = new Random().nextInt(101);
        int data2 = new Random().nextInt(101);
        js.put("concentration",data);
        js.put("brainWaves",data2);
        PrintWriter out = response.getWriter();
        out.print(js);
    }
}
