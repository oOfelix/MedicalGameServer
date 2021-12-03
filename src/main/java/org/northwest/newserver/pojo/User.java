package org.northwest.newserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class User {
    private String uuid;
    private String userName;
    private String password;
    private String realName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private int sex;
    private int age;
}
