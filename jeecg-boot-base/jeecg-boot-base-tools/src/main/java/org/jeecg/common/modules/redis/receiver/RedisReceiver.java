package org.jeecg.common.modules.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.modules.redis.listener.JeecgRedisListerer;
import org.jeecg.common.util.SpringContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 * todo 4.22
 * 这个listener和receiver有什么关系
 * 作用是什么
 * listener为监听器
 * 这个接收器的目的在哪里
 * 为什么都是实现的onMessage
 *
 * 这个RedisReceiver是在RedisConfig中的commonListenerAdapter
 * 注入的
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        //拿到handlerName
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        //在IOC容器中拿出RedisListener
        JeecgRedisListerer messageListener = SpringContextHolder.getHandler(handlerName.toString(), JeecgRedisListerer.class);
        //如果不为空，那就使用onmessage
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
