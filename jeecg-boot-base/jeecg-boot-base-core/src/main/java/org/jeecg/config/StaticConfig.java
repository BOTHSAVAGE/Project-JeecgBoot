package org.jeecg.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 设置静态参数初始化
 * todo 4.16
 * 全局静态参数可以放再IOC中注入
 */
@Component
@Data
public class StaticConfig {

    @Value("${jeecg.oss.accessKey}")
    private String accessKeyId;

    @Value("${jeecg.oss.secretKey}")
    private String accessKeySecret;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;


    /*@Bean
    public void initStatic() {
       DySmsHelper.setAccessKeyId(accessKeyId);
       DySmsHelper.setAccessKeySecret(accessKeySecret);
       EmailSendMsgHandle.setEmailFrom(emailFrom);
    }*/

}
