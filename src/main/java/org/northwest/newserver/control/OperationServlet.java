package org.northwest.newserver.control;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.northwest.newserver.pojo.Message;
import org.northwest.newserver.service.DataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
@Slf4j
public class OperationServlet {
    @Autowired
    DataHandler dataHandler;

    @RequestMapping("/start")
    public String Test(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("username","felix");
        return "index.html";
    }

    @GetMapping("/OperationServlet")
    public void doAlgorithm(HttpServletRequest request){
        String ID = request.getParameter("ID");
        String Payload = request.getParameter("Payload");
        log.info("收到算法数据"+ "id:" + ID + "Payload" + Payload);
        if (ID == null || Payload == null) {
            return;
        }
        dataHandler.dataRec(ID, Payload);
    }

    @PostMapping("/operation")
    public JSONObject doClient(@RequestBody Message message, HttpServletRequest req){
        return dataHandler.solve(message, req);
    }
}
