package org.northwest.newserver.pojo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;

@Data
@Component
@RequestScope
public class Record {
    private String rid;
    private String userName;
    private String record;
    private String datetime;
}
