package example.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 解析JSON数据
 * @author felix
 */
public class GetJsonObject {
    public JSONObject getObject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        InputStream ios = request.getInputStream();
        InputStreamReader ior = new InputStreamReader(ios);
        BufferedReader bfr = new BufferedReader(ior);


        StringBuilder sb = new StringBuilder(); // 接受结果
        String temp;
        while ((temp = bfr.readLine()) != null) {
            sb.append(temp);
        }
        System.out.println("JSON:" + sb);
        bfr.close();
        ior.close();
        ios.close();
        return JSON.parseObject(sb.toString());
    }
}