package com.zds.springboot.config.websocket;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hugaojun Email:nat17185546@163.com
 * @create 2023-06-14-[上午 10:26]-周三
 */
@ServerEndpoint(value = "/websocket")
@Component
@Slf4j
public class WebSocket {

    //实例一个session，这个session是websocket的session
    private Session session;

    //存放websocket的集合（本次demo不会用到，聊天室的demo会用到）
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //前端请求时一个websocket时
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    //前端关闭时一个websocket时
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    //前端向后端发送消息
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    //新增一个方法用于主动向客户端发送消息
    public static void sendMessage(Object message) {
        for (WebSocket webSocket: webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(JSONUtil.toJsonStr(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
