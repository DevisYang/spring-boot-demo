package com.cgutech.spring.controller;

import com.cgutech.spring.bean.Greeting;
import com.cgutech.spring.bean.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    private Logger logger = LoggerFactory.getLogger(GreetingController.class);

    // 可以使用该实例对象还进行推送消息,如: template.convertAndSend("Hello, 程序员锡哥!");
    public final SimpMessagingTemplate template;

    public GreetingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings") // 该注解会使得函数在返回之后，将返回的结果转为JSON字符串，并将消息推送到"/topic/greetings"主题
    public Greeting greeting(HelloMessage message) throws Exception {
        logger.info("收到数据：" +message.getName());
        Thread.sleep(1000);

        // template.convertAndSend("/topic/greetings", "Hello, 程序员锡哥!");
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
