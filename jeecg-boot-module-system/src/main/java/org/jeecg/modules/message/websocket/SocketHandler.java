package org.jeecg.modules.message.websocket;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.CommonSendStatus;
import org.jeecg.common.modules.redis.listener.JeecgRedisListerer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听消息(采用redis发布订阅方式发送消息)
 *
 * #todo 4.22
 * 实现jeecgRedislistener的接口（这个接口只是一个规范而已）
 * 里面放了一个websocket (ServerEndpoint 就是可以当controller使用)
 *
 */
@Slf4j
@Component
public class SocketHandler implements JeecgRedisListerer {

    @Autowired
    private WebSocket webSocket;


    /**
     * 这个类是在RedisReciver中被调用的
     * 最终redis消息订阅是在这里被处理的
     * @param map
     */
    @Override
    public void onMessage(BaseMap map) {
        //打印日志
        log.info("【SocketHandler消息】Redis Listerer:" + map.toString());
        //获取到用户和消息
        String userId = map.get("userId");
        String message = map.get("message");
        //这里可以看到redis消息订阅的目的是为了给用户推送消息
        if (ObjectUtil.isNotEmpty(userId)) {
            webSocket.pushMessage(userId, message);
            //app端消息推送
            webSocket.pushMessage(userId+CommonSendStatus.APP_SESSION_SUFFIX, message);
        } else {
            webSocket.pushMessage(message);
        }

    }
}