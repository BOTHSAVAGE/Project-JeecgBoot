package org.jeecg.config;

import org.jeecg.common.constant.CommonConstant;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 微服务环境加载条件
 * todo 4.16
 * 条件注入注解中要使用condition
 * 写法就是实现这个condition
 * 重写matches方法
 */
public class JeecgCloudCondition implements Condition {

    /**
     *
     * @param context 应用上下文，也就是spring的整个容器
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Object object = context.getEnvironment().getProperty(CommonConstant.CLOUD_SERVER_KEY);
        //如果没有服务注册发现的配置 说明是单体应用
        if(object==null){
            return false;
        }
        return true;
    }
}
