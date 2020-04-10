package com.cgutech.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// @ServerEndpoint("/chat")
//@Component
public class WebChatSocket {

    private static Map<String, WebChatSocket> sessions = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(WebChatSocket.class);
    private String sessionId;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        sessionId = session.getId();
        sessions.put(sessionId, this);
        logger.info("客户端在线数: " + sessions.size());
    }

    @OnClose
    public void onClose() {
        if(sessions.containsKey(sessionId)) {
            sessions.get(sessionId);
        }
        sessions.remove(sessionId);
        logger.info("客户端在线数: " + sessions.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("客户端[" + session.getId() +"]数据：" + message);
        write(message, session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("客户端[" + session.getId() +"]连接断开");
        sessions.remove(session.getId());
        logger.info("客户端在线数: " + sessions.size());
    }

    public static void write(String data, Session session) {
        for (Map.Entry<String, WebChatSocket> entry : sessions.entrySet()) {
            try {
                if(!entry.getKey().equals(session.getId())) {
                    session.getBasicRemote().sendText(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
