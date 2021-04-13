package org.jeecg.common.system.base.service.impl;

import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.common.system.base.service.JeecgService;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ServiceImpl基类
 * @Author: dangzhenghui@163.com
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 *
 * 写完IService
 * 就是ServiceImpl
 *
 * 这里的文件和JeecgService定义的目的都差不多
 * 就是为了重命名一下我感觉
 *
 * 上面的话错误
 *
 * JeecgServiceImpl的目的为了抽取整个系统的service层的公共方法
 * 这里没有公共方法所以没有抽取
 * 测试
 */
@Slf4j
public class JeecgServiceImpl<M extends BaseMapper<T>, T extends JeecgEntity> extends ServiceImpl<M, T> implements JeecgService<T> {

}
