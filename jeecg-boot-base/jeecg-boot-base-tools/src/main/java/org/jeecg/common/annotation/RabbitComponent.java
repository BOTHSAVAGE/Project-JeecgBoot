package org.jeecg.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author:zyf
 * @Date:2019-07-31 10:43
 * @Description: 消息队列初始化注解
 *
 * todo 4.23
 * 这里的aliasfor表示的别名
 * 在4月23日还没有涉及到mq内容
 **/
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RabbitComponent {
    @AliasFor(
            annotation = Component.class
    )
    String value();
}
