package org.jeecg.config.mybatis;

import lombok.extern.slf4j.Slf4j;

/**
 * 多租户 tenant_id存储器
 * todo 4.16
 * 这里使用了thradlocal，在每一个线程中都拷贝一份变量的副本
 * 不清楚
 */
@Slf4j
public class TenantContext {

    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenant(String tenant) {
        log.debug(" setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getTenant() {
        return currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();
    }
}