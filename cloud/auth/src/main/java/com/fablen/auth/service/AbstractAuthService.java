package com.fablen.auth.service;

import com.fablen.auth.dto.LoginRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 1. 定义通用抽象方法
 * 抽象服务类 AbstractAuthService，定义登录和权限加载逻辑的接口。
 *
 * @author zhangbaosheng
 */
public interface AbstractAuthService {
    /**
     * 登录逻辑
     *
     * @return UserDetails 用户详情
     */
    UserDetails authenticate(LoginRequest loginRequest);

    /**
     * 加载用户权限
     *
     * @param userDetails 用户详情
     * @return 用户权限列表
     */
    Collection<? extends GrantedAuthority> loadUserPermissions(UserDetails userDetails);
}