package com.cloud.auth.util;

import com.cloud.auth.service.AbstractAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 3. 动态选择登录方式
 * 3.1 工厂类 根据 authType 返回对应的 AbstractAuthService 实现。
 *
 * @author zhangbaosheng
 */
@Component
public class AuthServiceFactory {
    @Autowired
    private Map<String, AbstractAuthService> authServices;

    public AbstractAuthService getAuthService(String authType) {
        AbstractAuthService authService = authServices.get(authType + "AuthService");
        if (authService == null) {
            throw new IllegalArgumentException("Invalid authType: " + authType);
        }
        return authService;
    }
}
