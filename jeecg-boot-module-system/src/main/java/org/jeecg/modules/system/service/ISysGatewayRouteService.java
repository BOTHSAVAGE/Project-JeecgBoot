package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysGatewayRoute;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date:   2020-05-26
 * @Version: V1.0
 *
 * todo 4.14
 * 这里应该是zuul中的路由，也就是微服务
 * 为什么要把路由放在redis中操作
 * 这个需求值得研究
 */
public interface ISysGatewayRouteService extends IService<SysGatewayRoute> {

    /**
     * 添加所有的路由信息到redis
     * @param key
     */
     void addRoute2Redis(String key);

    /**
     * 删除路由
     * @param id
     */
     void deleteById(String id);

    /**
     * 保存路由配置
     * @param array
     */
    void updateAll(JSONObject array);

    /**
     * 清空redis中的route信息
     */
    void clearRedis();

}
