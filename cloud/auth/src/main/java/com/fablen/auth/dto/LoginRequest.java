package com.fablen.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String authType; // "github"、"password"、"ldap" 等
    private String username; // 普通登录/LDAP
    private String password; // 普通登录/LDAP
    private String code;     // GitHub OAuth2登录
    private String state;    // GitHub OAuth2登录
}