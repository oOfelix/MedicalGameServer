package org.northwest.newserver.pojo;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Message {
    private String uuid;    //uuid
    private String userName;    // 用户名
    private String password;    // 密码
    private int operationCode;  // 操作码
    private boolean status; // 操作逻辑状态
    private int errorCode;  // 具体情况
    private int points;     // 分数
    private String state;   // 故事场景存档
    private String id;      // 算法ID 0x01 0x02 0x03
    private String payload; // 负载因子
    private String msg;     // 消息
    private String record;  // 记录
    private String rid;     // 记录id
    private List<User> userList;    // 用户列表
    private String realName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private int sex;
    private int age;
}
