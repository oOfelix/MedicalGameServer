package example.controller;

import com.alibaba.fastjson.JSONObject;
import example.utils.DataHandler;
import example.utils.GetJsonObject;
import example.utils.HashData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

@WebServlet(name = "OperationServlet", value = "/OperationServlet")
public class OperationServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OperationServlet.class);
    private static final DataHandler dataHandler = new DataHandler();
    //算法 GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("a get request");
        String ID = request.getParameter("ID");
        String Payload = request.getParameter("Payload");
//        System.out.println( request.getQueryString());
        if(ID == null || Payload == null)
            return;
//        System.out.println(ID + Payload);
        dataHandler.dataRec(ID,Payload);
    }

    //游戏端 POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 解析Post请求拿取Post请求体信息并转成json格式 使用Springboot则不需要，可直接拿去
        JSONObject jsonObject = new GetJsonObject().getObject(request, response);
        //拿到输出流 返回消息
        PrintWriter out = response.getWriter();
        //拿到处理器 具体给每个请求处理
        DataHandler dataHandler = new DataHandler();
        //json 格式不对直接返回
        if(jsonObject == null || !jsonObject.containsKey("operationCode")){
            out.print(MessageFormat.format("'{'state:{0},operationCode:{1},errorCode:{2},msg:{3}'}'",false,0,500,"格式错误"));
            return;
        }
        switch(jsonObject.getInteger("operationCode")){
            //注册
            case 1:{
                out.print(dataHandler.signUp(jsonObject));
            }
            break;
            //登陆验证  记录当前用户的信息 session
            case 3:{
                String res = dataHandler.signIn(jsonObject);
                JSONObject js = JSONObject.parseObject(res);
                logger.info(String.valueOf(js));
                if (js.getBoolean("state")){
                    HttpSession session = request.getSession();
                    session.setAttribute("userName",jsonObject.getString("userName"));
                    logger.info((String) session.getAttribute("userName"));
                }
                out.print(res);
            }
            break;
            //客户端请求语音和动作信息
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
                String s = dataHandler.getPoint(jsonObject);
                out.print(s);
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
                out.print(dataHandler.findAll());
            }break;
            //获取的当前用户的session信息
            case 17:{
                HttpSession session = request.getSession();
                String userName = (String) session.getAttribute("userName");
                JSONObject res = new JSONObject();
                if (session.getAttribute("userName") == null){
                    res.put("userName","noLogin");
                }else {
                    res.put("userName",userName);
                }
                res.put("operationCode",18);
//                logger.info(userName);
                out.print(res);
            }break;
            // 请求脑电数据信息
            case 19:{
                //算法端脑电专注度数据
                String res = HashData.getHashValue("0x03","ss");
                assert res != null;
                String[] ret = res.split("-");
                JSONObject js = new JSONObject();
                if(Integer.parseInt(ret[0]) > 100){
                    js.put("concentration","-1");
                    js.put("brainWaves","-1");
                }else{
                    js.put("concentration",ret[0]);
                    js.put("brainWaves",ret[1]);
                }
                js.put("state",true);
                out.print(js);

//                模拟
//                JSONObject res = new JSONObject();
//                int data = new Random().nextInt(101);
//                int data2 = new Random().nextInt(101);
//                res.put("concentration",data);
//                res.put("brainWaves",data2);
//                res.put("operationCode",20);
//                res.put("state",true);
//                out.print(res);
            }break;
            default:out.print(MessageFormat.format("'{'state:{0},operationCode:{1},errorCode:{2},msg:{3}'}'",false,0,500,"格式错误"));
        }
    }
}
