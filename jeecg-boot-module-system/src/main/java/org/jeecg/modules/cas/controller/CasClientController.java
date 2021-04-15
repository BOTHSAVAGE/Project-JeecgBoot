package org.jeecg.modules.cas.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.cas.util.CASServiceUtil;
import org.jeecg.modules.cas.util.XmlUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * CAS单点登录客户端登录认证
 * </p>
 *
 * @Author zhoujf
 * @since 2018-12-20
 *
 * todo  4.15
 * 收藏 单点登录的实现
 * 单点登录
 * 单点登录意味着有一个公用的认证模块
 */
@Slf4j
@RestController
@RequestMapping("/sys/cas/client")
public class CasClientController {

	@Autowired
	private ISysUserService sysUserService;//todo 4.15
	@Autowired
    private ISysDepartService sysDepartService;//todo 4.15
	@Autowired
    private RedisUtil redisUtil;//todo 4.15
	
	@Value("${cas.prefixUrl}")
    private String prefixUrl;

	/**
	 * 相当于接口的登录
	 * @param ticket 凭证
	 * @param service 服务
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/validateLogin")
	public Object validateLogin(@RequestParam(name="ticket") String ticket,
								@RequestParam(name="service") String service) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		log.info("Rest api login.");//todo 登录前打一个日志
		try {
			String validateUrl = prefixUrl+"/p3/serviceValidate";//todo 这里应该是验证中心的URL
			String res = CASServiceUtil.getSTValidate(validateUrl, ticket, service);
			log.info("res."+res);
			final String error = XmlUtils.getTextForElement(res, "authenticationFailure");
			if(StringUtils.isNotEmpty(error)) {
				throw new Exception(error);
			}
			final String principal = XmlUtils.getTextForElement(res, "user");//解析xml获取i西南西
			if (StringUtils.isEmpty(principal)) {
	            throw new Exception("No principal was found in the response from the CAS server.");
	        }
			log.info("-------token----username---"+principal);
		    //1. 校验用户是否有效
	  		SysUser sysUser = sysUserService.getUserByName(principal);
	  		result = sysUserService.checkUserIsEffective(sysUser);
	  		if(!result.isSuccess()) {
	  			return result;
	  		}
	 		String token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword());
	 		// 设置超时时间
	 		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
	 		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

	 		//获取用户部门信息
			JSONObject obj = new JSONObject();
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			obj.put("departs", departs);
			if (departs == null || departs.size() == 0) {
				obj.put("multi_depart", 0);
			} else if (departs.size() == 1) {
				sysUserService.updateUserDepart(principal, departs.get(0).getOrgCode());
				obj.put("multi_depart", 1);
			} else {
				obj.put("multi_depart", 2);
			}
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			result.setResult(obj);
			result.success("登录成功");
	  		
		} catch (Exception e) {
			//e.printStackTrace();
			result.error500(e.getMessage());
		}
		return new HttpEntity<>(result);
	}

	
}
