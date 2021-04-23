package org.jeecg.common.modules.redis.client;

import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * redis客户端
 *
 * 这个类的目的是什么 todo 4.13？
 *
 * #todo 4.23
 * 这个client就是表示客户端的意思
 * sendmessage就是为了发送消息
 * 对应的是消息订阅的模式推送端
 * 当receiver接收到的相同通道的消息的时候
 * 会进行websocket的推送
 *
 * 这个方法又是在websocket中调用的
 *
 *
 */
@Configuration
public class JeecgRedisClient {

    //注入传统的redis模板
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送消息
     *
     * @param handlerName
     * @param params
     */
    public void sendMessage(String handlerName, BaseMap params) {
        //1.把处理器名称放入map中
        params.put(GlobalConstants.HANDLER_NAME, handlerName);
        //2.把map中的值放入redis通道（这里是异步的，不需要redis确认收到消息）
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }


}
