package org.jeecg.common.modules.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.modules.redis.listener.JeecgRedisListerer;
import org.jeecg.common.util.SpringContextHolder;
import org.springframework.stereotype.Component;

/**
 * @check bothsavage
 * @lastUpdateTime 2021年6月16日
 *
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
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
