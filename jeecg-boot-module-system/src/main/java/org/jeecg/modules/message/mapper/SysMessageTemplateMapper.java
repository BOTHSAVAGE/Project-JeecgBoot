package org.jeecg.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.message.entity.SysMessageTemplate;

import java.util.List;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 *
 * 使用的是MP+提供的通用的BaseMapper
 * 依然是传入T
 * T要满足MP+规范
 *
 * BaseMapper 满足最常用的增删改查
 * 后面的mapper自己定义
 *
 * 这里使用的是注解的方式去定义
 *
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {
    @Select("SELECT * FROM SYS_SMS_TEMPLATE WHERE TEMPLATE_CODE = #{code}")
    List<SysMessageTemplate> selectByCode(String code);
}
