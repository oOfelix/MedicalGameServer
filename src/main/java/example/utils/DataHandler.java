package example.utils;

import com.alibaba.fastjson.JSONObject;
import example.domain.User;

import java.util.List;
import java.util.Map;

public class DataHandler{
    public String findAll(){
        List<Map<String,Object>> userList = DBUtils.findAll();
        return JSONObject.toJSONString(userList);
    }

    /**
     * 注册
     * operationCode 1
     * @return String
     * @author felix
     */
    public String signUp(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        int code = DBUtils.signUp(user);
        if(code == 0){
            return "{\"state\":true,\"operationCode\":2,\"errorCode\":0,\"msg\":\"注册成功\"}";
        }else if(code == -3){
            return "{\"state\":false,\"operationCode\":2,\"errorCode\":-3,\"msg\":\"账号已存在\"}";
        }else{
            return "{\"state\":false,\"operationCode\":2,\"errorCode\":500,\"msg\":\"服务器错误\"}";
        }
    }

    /**
     * 登陆
     * operationCode 3
     * @return String
     * @author felix
     */
    public String signIn(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        int code = DBUtils.signIn(user);
        if(code == 0){
            return "{\"state\":true,\"operationCode\":4,\"errorCode\":0,\"msg\":\"验证成功\"}";
        }else if(code == -1){
            return "{\"state\":false,\"operationCode\":4,\"errorCode\":-1,\"msg\":\"密码错误\"}";
        }
        else if(code == -2){
            return "{\"state\":false,\"operationCode\":4,\"errorCode\":-2,\"msg\":\"账户不存在\"}";
        }else{
            return "{\"state\":false,\"operationCode\":4,\"errorCode\":500,\"msg\":\"服务器错误\"}";
        }
    }

    /**
     * 客户端请求数据
     * operationCode 5
     * @author nicer
     */
    public String dataSend(JSONObject jsonObject){
        String key = jsonObject.getString("checkType");
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        String storyID = jsonObject.getString("storyID");
        String field = jsonObject.getString("result");
        long reqTime = System.currentTimeMillis() - 2000;
        if (DBUtils.signIn(user) == 0 && HashData.exists(key,field)) {
            String timePayload = HashData.getHashValue(key,field);
            System.out.println(timePayload);
            assert timePayload != null;
            if (timePayload.isEmpty()) {
                return("{\"state\":false,\"operationCode\":6,\"checkType\":\"" + key + "\",\"result\":\"null\",\"storyID\":\"" + storyID + "\",\"errorCode\":-2,\"errorMsg\":\"key doesn't exists\"}");
            }
            long time = Long.parseLong(timePayload);
            if(time >= reqTime && time <= (reqTime + 12000)) {
                return ("{\"state\":true,\"operationCode\":6,\"checkType\":\"" + key + "\",\"result\":\"" + field + "\",\"storyID\":" + storyID + "}");
            }
            else{
                return("{\"state\":false,\"operationCode\":6,\"checkType\":\"" + key + "\",\"result\":\"null\",\"storyID\":\"" + storyID + "\",\"errorCode\":-2,\"errorMsg\":\"key doesn't exists\"}");
            }
        }
        else {
            if(DBUtils.signIn(user) != 0)
                return("{\"state\":false,\"operationCode\":6,\"checkType\":\"" + key + "\",\"result\":\"null\",\"storyID\":\"" + storyID + "\",\"errorCode\":-1,\"errorMsg\":\"username or password error\"}");
            else
                return("{\"state\":false,\"operationCode\":6,\"checkType\":\"" + key + "\",\"result\":\"null\",\"storyID\":\"" + storyID + "\",\"errorCode\":-2,\"errorMsg\":\"key doesn't exists\"}");
        }
    }

    /**
     * 客户端保存积分
     * operationCode 7
     * @return String
     * @author felix
     */
    public String savaPoint(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        int code = DBUtils.signIn(user);
        int points = jsonObject.getInteger("points");
        if(code == -1) {
            return "{\"state\":false,\"operationCode\":8,\"errorCode\":-1,\"errorMsg\":\"密码错误\"}";
        }
        else if(code == -2) {
            return "{\"state\":false,\"operationCode\":8,\"errorCode\":-2,\"errorMsg\":\"账号未注册\"}";
        }
        else if(code == 0 && DBUtils.savePoints(points, user.getUserName()) == 0) {
            return "{\"state\":true,\"operationCode\":8,\"errorCode\":0,\"msg\":\"录入成功\"}";
        }else {
            return "{\"state\":false,\"operationCode\":8,\"errorCode\":500,\"errorMsg\":\"服务器错误\"}";
        }
    }

    /**
     * 客户端请求积分
     * operationCode 9
     * @return String
     * @author felix
     */
    public String getPoint(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        int points = DBUtils.getPoint(user.getUserName());
        int code = DBUtils.signIn(user);
        if(code == 0) {
             return "{\"state\":true,\"operationCode\":10,\"point\":" + points + ",\"errorCode\":0,\"msg\":\"获取成功\"}";
        }
        else if(code == -1) {
            return "{\"state\":false,\"operationCode\":10,\"result\":-1,\"errorCode\":-1,\"errorMsg\":\"密码错误\"}";
        }
        else if(code == -2) {
            return "{\"state\":false,\"operationCode\":10,\"result\":-1,\"errorCode\":-2,\"errorMsg\":\"账号未注册\"}";
        }
        else {
            return "{\"state\":false,\"operationCode\":10,\"result\":-1,\"errorCode\":500,\"errorMsg\":\"服务器错误\"}";
        }
    }

    /**
     * 客户端请求存档
     * operationCode 11
     * @return String
     * @author felix
     */
    public String savaStatus(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        String status = jsonObject.getString("status");
        int code = DBUtils.signIn(user);
        if(code == 0 && DBUtils.savaStatus(status,user.getUserName()) == 0) {
            return "{\"state\":true,\"operationCode\":12,\"msg\":\"录入成功\"}";
        }
        else if(code == -1) {
            return "{\"state\":false,\"operationCode\":12,\"errorCode\":-1,\"errorMsg\":\"密码错误\"}";
        }
        else if(code == -2) {
            return "{\"state\":false,\"operationCode\":12,\"errorCode\":-2,\"errorMsg\":\"账号未注册\"}";
        }
        else {
            return "{\"state\":false,\"operationCode\":12,\"errorCode\":500,\"errorMsg\":\"服务器错误\"}";
        }

    }

    /**
     * 客户端请求存档数据
     * operationCode 13
     * @return String
     * @author felix
     */
    public String getStatus(JSONObject jsonObject){
        User user = new User(jsonObject.getString("userName"), jsonObject.getString("password"));
        int code = DBUtils.signIn(user);
        String status = DBUtils.getStatus(user.getUserName());
        System.out.println("qqq");
        if(code == 0 && status != null && !status.isEmpty()) {
            status =  "{\"state\":true,\"operationCode\":14,\"status\":"  + status  + ",\"errorCode\":0,\"msg\":\"获取成功\"}";
            JSONObject pa = JSONObject.parseObject(status);
            System.out.println("222");
            System.out.println(status);
            return pa.toString();
        }
        else if(code == -1) {
            return "{\"state\":false,\"operationCode\":14,\"result\":-1,\"errorCode\":-1,\"errorMsg\":\"密码错误\"}";
        }
        else if(code == -2) {
            return "{\"state\":false,\"operationCode\":14,\"result\":-1,\"errorCode\":-2,\"errorMsg\":\"账号未注册\"}";
        }
        else {
            return "{\"state\":false,\"operationCode\":14,\"result\":-1,\"errorCode\":500,\"errorMsg\":\"服务器错误\"}";
        }
    }

    /**
     * 算法接受数据
     * operationCode
     * @author nicer
     */
    public void dataRec(String ID,String Payload){
        long getTime = System.currentTimeMillis();
        String time = String.valueOf(getTime);
        if(Payload.equals("-1") || Payload.equals(""))
            return;
        if(ID.equals("0x01") || ID.equals("0x02") || ID.equals("0x03"))
        {
            HashData.addHash(ID,Payload,time);
        }
    }
}
