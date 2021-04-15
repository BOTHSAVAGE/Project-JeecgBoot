package org.jeecg.config.init;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.config.JeecgCloudCondition;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @desc: 启动程序，初始化路由配置
 * @author: flyme
 *
 * todo 4.14
 * 这里的有条件注入
 * 然后使用的是监听者模式，在项目启动的时候就会开始执行
 *
 * todo 4.15 上班打卡
 */
@Slf4j
@Component
@Conditional(JeecgCloudCondition.class)
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {


    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;//todo 4.14

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        log.info(" 服务已启动，初始化路由配置 ###################");
        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf("AnnotationConfigServletWebServerApplicationContext") > -1) {
            sysGatewayRouteService.addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
        }

    }

    //todo 4.14 这里就是listener加载的优先级
    @Override
    public int getOrder() {
        return 1;
    }
}
