package com.cloud.auth.controller;

import com.cloud.auth.dto.LoginRequest;
import com.cloud.auth.service.AbstractAuthService;
import com.cloud.auth.util.AuthServiceFactory;
import com.cloud.common.ApiResponse;
import com.cloud.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 统一登录接口，根据 authType 动态调用不同认证服务。
 * 支持账号密码、LDAP、GitHub等多种登录方式。
 * 登录成功返回JWT Token。
 *
 * @author zhangbaosheng
 */
@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AuthServiceFactory authServiceFactory;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<String>> index() {
        return ResponseEntity.ok(ApiResponse.success("欢迎访问鉴权中心！请先登录"));
    }

    /**
     * 统一登录接口，支持多种认证方式（账号密码、GitHub、LDAP等）。
     * 前端需传递authType，及所需参数。
     */
    @PostMapping("/api/login")
    public ResponseEntity<ApiResponse<String>> authenticate(@RequestBody LoginRequest loginRequest) {
        String authType = loginRequest.getAuthType();

        AbstractAuthService authService = authServiceFactory.getAuthService(authType);

        // 统一DTO参数传递，具体实现类自行解析需要的参数
        UserDetails userDetails = authService.authenticate(loginRequest);

        Collection<? extends GrantedAuthority> permissions = authService.loadUserPermissions(userDetails);

        // 这里只取第一个权限作为role，按需扩展
        String role = permissions.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        // 生成JWT Token
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        return ResponseEntity.ok(ApiResponse.success(token));
    }
}