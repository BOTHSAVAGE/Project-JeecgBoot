package org.jeecg.common.system.base.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: Service基类
 * @Author: dangzhenghui@163.com
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 *
 * Iservice 是MP+的通用增删改查Service服务层
 * 要传入的T
 * T必须满足MP+的规范
 *
 *
 * 在这个里面应该有一些通用的服务于这个系统的接口规范
 *
 * 这里为空，说明没有这个需求
 */
public interface JeecgService<T> extends IService<T> {
}
