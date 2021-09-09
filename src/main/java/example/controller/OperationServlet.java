package example.controller;

import com.alibaba.fastjson.JSONObject;
import example.utils.DataHandler;
import example.utils.GetJsonObject;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "OperationServlet", value = "/OperationServlet")
public class OperationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //算法
        System.out.println("a get request");
        String ID = request.getParameter("ID");
        String Payload = request.getParameter("Payload");
        System.out.println( request.getQueryString());
        if(ID == null || Payload == null)
            return;
        System.out.println(ID + Payload);
        DataHandler dataHandler = new DataHandler();
        dataHandler.dataRec(ID,Payload);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //客户端
        System.out.println();
        JSONObject jsonObject = new GetJsonObject().getObject(request, response);
        PrintWriter out = response.getWriter();
        DataHandler dataHandler = new DataHandler();

        if(jsonObject == null || !jsonObject.containsKey("operationCode")){
            out.print("{\"state\":false,\"operationCode\":0,\"errorCode\":500,\"msg\":\"传输数据格式错误\"}");
            return;
        }
        int operationCode = jsonObject.getInteger("operationCode");
        System.out.println(operationCode);
        switch(operationCode){
            //注册
            case 1:{
                out.print(dataHandler.signUp(jsonObject));
            }
            break;
            //验证
            case 3:{
                out.print(dataHandler.signIn(jsonObject));
            }
            break;
            //客户端请求数据
            case 5:{
                out.print(dataHandler.dataSend(jsonObject));
            }
            break;
            //客户端保存积分
            case 7:{
                out.print(dataHandler.savaPoint(jsonObject));
            }
            break;
            //客户端请求积分
            case 9:{
                out.print(dataHandler.getPoint(jsonObject));
            }
            break;
            //客户端请求保存游戏存档
            case 11:{
                out.print(dataHandler.savaStatus(jsonObject));
            }
            break;
            //客户端请求游戏存档
            case 13:{
                out.print(dataHandler.getStatus(jsonObject));
            }
            break;
            //客户请求用户信息
            case 15:{
                System.out.println(dataHandler.findAll());
                out.print(dataHandler.findAll());
            }
            break;
            default:out.print("{\"state\":false,\"operationCode\":0,\"errorCode\":500,\"msg\":\"服务器错误\"}");
        }
    }
}
