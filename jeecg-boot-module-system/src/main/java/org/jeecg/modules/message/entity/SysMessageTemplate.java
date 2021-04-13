package org.jeecg.modules.message.entity;

import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 *
 * @Accessors(chain = true)的目的是为了方便级联操作，set会返回当前对象
 *
 *
 * 当前实体类和MP+绑定
 * @TableName 为mybaits+的注解
 *
 * @Excel注解的目的是方便导出Excle文件
 * 该注解为自定义注解，内部原来应该不复杂，但是思想可以，后面研究一下可不可以做POI的高级操作
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sms_template")
public class SysMessageTemplate extends JeecgEntity{
	/**模板CODE*/
	@Excel(name = "模板CODE", width = 15)
	private java.lang.String templateCode;
	/**模板标题*/
	@Excel(name = "模板标题", width = 30)
	private java.lang.String templateName;
	/**模板内容*/
	@Excel(name = "模板内容", width = 50)
	private java.lang.String templateContent;
	/**模板测试json*/
	@Excel(name = "模板测试json", width = 15)
	private java.lang.String templateTestJson;
	/**模板类型*/
	@Excel(name = "模板类型", width = 15)
	private java.lang.String templateType;
}
