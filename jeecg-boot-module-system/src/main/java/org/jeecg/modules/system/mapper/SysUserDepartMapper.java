package org.jeecg.modules.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * todo 4.14
 * 用户权限 mp+通用
 * xml模式
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{
	
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
