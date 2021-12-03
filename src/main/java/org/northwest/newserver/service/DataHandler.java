package org.northwest.newserver.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.northwest.newserver.mapper.UserMapper;
import org.northwest.newserver.pojo.Message;
import org.northwest.newserver.pojo.Record;
import org.northwest.newserver.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class DataHandler {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private User user;

    public JSONObject solve(Message msg, HttpServletRequest req){
        JSONObject message = null;
        int code = msg.getOperationCode();
        switch (code){
            case 1:message = signUp(msg);break;
            case 3:message = signIn(msg);break;
            case 5:message = dataSend(msg);break;
            case 7:message = savaPoint(msg);break;
            case 9:message = getPoint(msg);break;
            case 11:message = saveState(msg);break;
            case 13:message = getState(msg);break;
            case 15:message = getUserInfo(msg);break;
            case 17:message = getUserList(msg);break;
            case 19:message = getBC(msg);break;
            case 51:message = backendStart(msg, req);break;
            case 53:message = saveGameRecord(msg,req);break;
            case 55:message = getGameReport(msg);break;
            case 57:message = updateUserInfo(msg);break;
            default:{
                message = new JSONObject();
                message.put("status", false);
                message.put("operationCode", 500);
                message.put("msg", "无效请求");
            }
        }
        return message;
    }



    // 接受算法端的数据，并保存
    public void dataRec(String ID, String Payload){
        String time = String.valueOf(System.currentTimeMillis());
        if("0x01".equals(ID) || "0x02".equals(ID) || "0x03".equals(ID)){
            HashData.addHash(ID, Payload, time);
        }
    }

    /**
     * 注册
     * operationCode 1
     * @return String
     * @author felix
     */
    public JSONObject signUp(Message message){
        JSONObject jsonObject = new JSONObject();
        User isSignUp = userMapper.getUser(message.getUserName());
        if(isSignUp != null){
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 1);
            jsonObject.put("msg", "账号已存在");
        }else{
            String uuid = UUID.randomUUID().toString();
            int addUser = userMapper.addUser(uuid, message.getUserName(), message.getPassword());
            int addProgress = userMapper.addProgress(message.getUserName());
            if(addProgress + addUser == 2){
                jsonObject.put("status", true);
                jsonObject.put("msg", "注册成功");
            }else{
                jsonObject.put("status", false);
                jsonObject.put("errorCode", 500);
                jsonObject.put("msg", "数据库错误");
            }
        }
        jsonObject.put("operationCode", 2);
        return jsonObject;
    }

    /**
     * 登陆
     * operationCode 3
     * @return String
     * @author felix
     */
    public JSONObject signIn(Message message){
        JSONObject jsonObject = new JSONObject();
        String rid = UUID.randomUUID().toString();
        User getUser = userMapper.getUser(message.getUserName());
        if(getUser == null){
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 2);
            jsonObject.put("msg", "账号不存在");
        }else if(Objects.equals(getUser.getPassword(), message.getPassword())){
            jsonObject.put("status", true);
            jsonObject.put("msg", "登陆成功");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy年MM月dd日 HH:mm:ss");
            String datetime = dtf.format(LocalDateTime.now());
            int code = userMapper.addRecord(rid, message.getUserName(), "", datetime);
            jsonObject.put("rid", rid);
        }else if(!Objects.equals(getUser.getPassword(), message.getPassword())){
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 3);
            jsonObject.put("msg", "密码错误");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 500);
            jsonObject.put("msg", "服务器错误");
        }
        jsonObject.put("operationCode", 4);
        return jsonObject;
    }

    /**
     * 客户端请求数据
     * operationCode 5
     * @author nicer
     */
    public JSONObject dataSend(Message message){
        JSONObject jsonObject = new JSONObject();
        String reqID = message.getId();
        String reqPayload = message.getPayload();
        long reqTime = System.currentTimeMillis() - 3000;
        if (HashData.exists(reqID,reqPayload)) {
            String timePayload = HashData.getHashValue(reqID,reqPayload);
            assert timePayload != null;
            long time = Long.parseLong(timePayload);
            if(time >= reqTime && time <= (reqTime + 12000)) {
                message.setPoints(userMapper.getPoints(message.getUserName()) + 10);
                savaPoint(message);
                jsonObject.put("status", true);
                jsonObject.put("msg", "识别通过");
            }
            else{
                jsonObject.put("status", false);
                jsonObject.put("errorCode", 4);
                jsonObject.put("msg", "识别超时");
            }
        }else {
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 5);
            jsonObject.put("msg", "语言或者动作未达标");
        }
        jsonObject.put("operationCode", 6);
        jsonObject.put("id", reqID);
        jsonObject.put("payload", reqPayload);
        return jsonObject;
    }

    /**
     * 客户端保存积分
     * operationCode 7
     * @return String
     * @author felix
     */
    public JSONObject savaPoint(Message message){
        JSONObject jsonObject = new JSONObject();
        int points = message.getPoints();
        int code = userMapper.savePoints(points,message.getUserName());
        if(code == 1){
            jsonObject.put("status", true);
            jsonObject.put("msg", "分数保存成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 6);
            jsonObject.put("msg", "分数保存失败");
        }
        jsonObject.put("operationCode", 8);
        return jsonObject;
    }

    /**
     * 客户端请求积分
     * operationCode 9
     * @return String
     * @author felix
     */
    public JSONObject getPoint(Message message){
        JSONObject jsonObject = new JSONObject();
        int points = userMapper.getPoints(message.getUserName());
        jsonObject.put("status", true);
        jsonObject.put("points", points);
        jsonObject.put("msg", "分数获取成功");
        jsonObject.put("operationCode", 10);
        return jsonObject;
    }

    /**
     * 客户端请求存档
     * operationCode 11
     * @return String
     * @author felix
     */
    public JSONObject saveState(Message message){
        JSONObject jsonObject = new JSONObject();
        String state = message.getState();
        int code = userMapper.savaState(state,message.getUserName());
        if(code == 1){
            jsonObject.put("status", true);
            jsonObject.put("msg", "保存进度成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 6);
            jsonObject.put("msg", "保存进度失败");
        }
        jsonObject.put("operationCode", 12);
        return jsonObject;
    }

    /**
     * 客户端请求存档数据
     * operationCode 13
     * @return String
     * @author felix
     */
    public JSONObject getState(Message message){
        JSONObject jsonObject = new JSONObject();
        String state = userMapper.getStatus(message.getUserName());
        if(state != null){
            jsonObject.put("status", true);
            jsonObject.put("errorCode", 0);
            jsonObject.put("state", state);
            jsonObject.put("msg", "获取进度成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 7);
            jsonObject.put("msg", "获取进度失败");
        }
        jsonObject.put("operationCode", 14);
        return jsonObject;
    }

    /**
     * 请求用户个人信息
     * operationCode 15
     * @return String
     * @author felix
     */
    public JSONObject getUserInfo(Message message){
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> userInfo = userMapper.getUserInfo(message.getUserName());
        System.out.println(userInfo);
        if(userInfo != null){
            jsonObject.put("status", true);
            jsonObject.put("msg", "查询个人信息成功");
            jsonObject.put("userName", userInfo.get("userName"));
            jsonObject.put("realName", userInfo.get("realName"));
            jsonObject.put("state", userInfo.get("state"));
            jsonObject.put("uuid", userInfo.get("uuid"));
            jsonObject.put("address", userInfo.get("address"));
            jsonObject.put("age", userInfo.get("age"));
            jsonObject.put("sex", userInfo.get("sex"));
            jsonObject.put("phoneNumber", userInfo.get("phoneNumber"));
            jsonObject.put("points", userInfo.get("points"));
            jsonObject.put("birthday", userInfo.get("birthday"));

        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 8);
            jsonObject.put("msg", "查询个人信息失败");
        }

        jsonObject.put("operationCode", 16);
        return jsonObject;
    }

    /**
     * 请求用户个人信息
     * operationCode 17
     * @return String
     * @author felix
     */
    public JSONObject getUserList(Message message){
        JSONObject jsonObject = new JSONObject();
        List<User> allUser = userMapper.getAllUser();
        if(allUser != null){
            jsonObject.put("status", true);
            jsonObject.put("userList", allUser);
            jsonObject.put("msg", "查询用户列表信息成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 9);
            jsonObject.put("msg", "查询用户列表信息失败");
        }
        jsonObject.put("operationCode", 18);
        return jsonObject;
    }

    /**
     * 更新个人信息
     * operationCode 19
     * @return String
     * @author felix
     */

    public JSONObject getBC(Message message){
        JSONObject jsonObject = new JSONObject();
        String res = HashData.getHashValue("0x03","ss");
        assert res != null;
        String[] ret = res.split("-");
        if(Integer.parseInt(ret[0]) > 100){
            jsonObject.put("concentration","-1");
            jsonObject.put("brainWaves","-1");
            jsonObject.put("status",true);
        }else{
            jsonObject.put("concentration",ret[0]);
            jsonObject.put("brainWaves",ret[1]);
            jsonObject.put("status",true);
            jsonObject.put("msg", "请求脑电数据成功");
        }
        jsonObject.put("operationCode", 20);
        return jsonObject;
    }

    /**
     * 更新个人信息
     * operationCode 57
     * @return String
     * @author felix
     */
    public JSONObject updateUserInfo(Message message){
        JSONObject jsonObject = new JSONObject();
        user.setUserName(message.getUserName());
        user.setRealName(message.getRealName());
        user.setAddress(message.getAddress());
        user.setBirthday(message.getBirthday());
        user.setSex(message.getSex());
        user.setPhoneNumber(message.getPhoneNumber());
        user.setAge(message.getAge());
        if(userMapper.updateUserInfo(user) == 1){
            jsonObject.put("status", true);
            jsonObject.put("msg", "更新用户个人信息成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 10);
            jsonObject.put("msg", "更新用户个人信息失败");
        }
        jsonObject.put("operationCode", 58);
        return jsonObject;
    }


    /**
     * 后台初始化
     * operationCode 51
     * @return String
     * @author felix
     */
    public JSONObject backendStart(Message message, HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String userUuid = (String) req.getSession().getAttribute("userName");
        if(userUuid != null){
            jsonObject.put("status", true);
            jsonObject.put("msg", "后台启动成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 10);
            jsonObject.put("msg", "后台启动失败");
        }
        jsonObject.put("operationCode", 52);
        return jsonObject;
    }

    /**
     * 客户端保存记录
     * operationCode 53
     * @return String
     * @author felix
     */
    public JSONObject saveGameRecord(Message message, HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String clientRecord = message.getRecord();
        if(userMapper.saveGameRecord(message.getRid(), clientRecord) == 1){
            jsonObject.put("status", true);
            jsonObject.put("msg", "保存记录成功");
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 10);
            jsonObject.put("msg", "保存记录失败");
        }
        jsonObject.put("operationCode", 54);
        return jsonObject;
    }





//    public Message saveGameRecord(Message message, HttpServletRequest req){
//        user.setUuid(message.getUuid());
//        String clientRecord = message.getRecord();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime);
//
//        HttpSession session = req.getSession();
//        if(session.getAttribute("userName") != null){
//            GameContainer.gameBuffer.put("record",clientRecord);
//            message.setStatus(true);
//            message.setErrorCode(0);
//            message.setMsg("暂存记录成功");
//            log.info(GameContainer.gameBuffer.toString());
//        }else{
//            if(user.getUserName() != null && clientRecord != null && 1 == userMapper.saveGameRecord(user.getUuid(), (String) GameContainer.gameBuffer.get("record"))){
//                message.setStatus(true);
//                message.setErrorCode(0);
//                message.setMsg("保存记录成功");
//            }else{
//                message.setStatus(false);
//                message.setErrorCode(-1);
//                message.setMsg("保存记录失败");
//            }
//        }
//        message.setOperationCode(54);
//        return message;
//    }

    /**
     * 客户端获取记录
     * operationCode 55
     * @return String
     * @author felix
     */
    public JSONObject getGameReport(Message message){
        JSONObject jsonObject = new JSONObject();
        List<Record> recordList = userMapper.getGameRecord(message.getUserName());
        if(recordList != null){
            jsonObject.put("status", true);
            jsonObject.put("recordList", recordList);
            jsonObject.put("msg", "获取记录成功");
            System.out.println(jsonObject.get("recordList"));
        }else{
            jsonObject.put("status", false);
            jsonObject.put("errorCode", 10);
            jsonObject.put("msg", "获取记录失败");
        }
        jsonObject.put("operationCode", 56);
        return jsonObject;
    }

}
