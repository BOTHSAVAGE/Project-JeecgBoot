package org.jeecg.modules.message.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 *
 * todo 4 月 13 日
 * http://doc.jeecg.com/2044163
 * 总体来说的还是不难
 * ServerEndpoint这是一个特殊的注解，javax提供的目的就是为了websocket使用的
 * 这个websocket可以看作为一个工具类
 *
 * 收藏
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}") //此注解相当于设置访问URL
public class WebSocket {

    //websocket中的session
    private Session session;

    private String userId;

    private static final String REDIS_TOPIC_NAME = "socketHandler";

    //注入的redis相关的服务
    @Resource
    private JeecgRedisClient jeecgRedisClient;

    /**
     * 缓存 webSocket连接到单机服务class中（整体方案支持集群）
     */
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new HashMap<String, Session>();


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }


    /**
     * 服务端推送消息
     * todo 4.23 这里被redis消息监听者调用
     * @param userId
     * @param message
     */
    public void pushMessage(String userId, String message) {
        //sessionPool的本质就是一个hashmap，从pool中获取到的sesission
        Session session = sessionPool.get(userId);
        //判断session状态
        if (session != null && session.isOpen()) {
            try {
                //打印日志
                log.info("【websocket消息】 单点消息:" + message);
                //websocket发送消息接口
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务器端推送消息
     */
    public void pushMessage(String message) {
        try {
            webSockets.forEach(ws -> ws.session.getAsyncRemote().sendText(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnMessage
    public void onMessage(String message) {
        //todo 现在有个定时任务刷，应该去掉
        log.debug("【websocket消息】收到客户端消息:" + message);
        JSONObject obj = new JSONObject();
        //业务类型
        obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_CHECK);
        //消息内容
        obj.put(WebsocketConst.MSG_TXT, "心跳响应");
        for (WebSocket webSocket : webSockets) {
            webSocket.pushMessage(message);
        }
    }

    /**
     * 后台发送消息到redis
     * todo 4.23 这里  调用了   redis消息发布者
     * 这里不指定用户是群发消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        //1.打印日志
        log.info("【websocket消息】广播消息:" + message);
        //2.创建的自定义的map容器，放入部分信息
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", "");
        baseMap.put("message", message);
        //3.发布消息到redis的指定通道
        jeecgRedisClient.sendMessage(REDIS_TOPIC_NAME, baseMap);
    }

    /**
     * 此为单点消息
     *
     * @param userId
     * @param message
     */
    public void sendMessage(String userId, String message) {
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", userId);
        baseMap.put("message", message);
        jeecgRedisClient.sendMessage(REDIS_TOPIC_NAME, baseMap);
    }

    /**
     * 此为单点消息(多人)
     *
     * @param userIds
     * @param message
     */
    public void sendMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            sendMessage(userId, message);
        }
    }

}