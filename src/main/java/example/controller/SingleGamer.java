package example.controller;

import com.alibaba.fastjson.JSONObject;
import example.domain.User;
import example.utils.DBUtils;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/WebSocket")
public class SingleGamer {
    //MAP，用于存放每一个客户端连接
    private static final Map<String,SingleGamer> room = new ConcurrentHashMap<>();

    //session 对象，用于收发消息
    private Session session;

    private String userName;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
    }

    //OnClose 注解， 每一个关闭时的事件
    @OnClose
    public void OnClose(){
        room.remove(this.userName);
    }

    @OnMessage
    public void OnMessage(String msg){
        System.out.println(msg);
        JSONObject obj = JSONObject.parseObject(msg);
        int operationCode = Integer.parseInt(obj.getString("operationCode"));
        switch (operationCode){
            case 1:{
                User user = new User(obj.getString("userName"),obj.getString("password"));
                int code = DBUtils.signUp(user);
                switch (code){
                    case 0:session.getAsyncRemote().sendText("{\"state\":true,\"operationCode\":2,\"errorCode\":0,\"msg\":\"注册成功\"}");
                    break;
                    case -3:session.getAsyncRemote().sendText("{\"state\":false,\"operationCode\":2,\"errorCode\":-3,\"msg\":\"账号已存在\"}");
                    break;
                    case 500:session.getAsyncRemote().sendText("{\"state\":false,\"operationCode\":2,\"errorCode\":500,\"msg\":\"服务器错误\"}");
                    break;
                }
            }
            break;
            case 3:{
                User user = new User(obj.getString("userName"),obj.getString("password"));
                System.out.println("sign" + user);
                int code = DBUtils.signIn(user);
                switch (code){
                    case 0:{
                        session.getAsyncRemote().sendText("{\"state\":true,\"operationCode\":4,\"errorCode\":0,\"msg\":\"登陆成功\"}");
                        room.put(user.getUserName(),this);
                    }break;
                    case -1:session.getAsyncRemote().sendText("{\"state\":false,\"operationCode\":4,\"errorCode\":-1,\"msg\":\"密码错误\"}");
                    break;
                    case -2:session.getAsyncRemote().sendText("{\"state\":false,\"operationCode\":4,\"errorCode\":-2,\"msg\":\"账号未注册\"}");
                    break;
                }
            }
            break;
            case 5:session.getAsyncRemote().sendText("你好");
            break;
            case 7:
        }
    }

    // 服务器端错误时的，事件处理
    @OnError
    public void onError(Throwable t) throws Throwable{
        System.out.println("WebSocket 服务端错误"+t.getMessage());
    }
    public void broadcast(String msg){
        Set<String> keySet = room.keySet();
        for (String s : keySet) {
            room.get(s).session.getAsyncRemote().sendText("大家过年好");
        }
    }
}

