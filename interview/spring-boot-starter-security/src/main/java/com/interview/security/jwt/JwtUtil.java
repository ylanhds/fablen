package com.interview.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * JWT令牌提供者组件
 * 主要功能：
 * 1. 生成JWT令牌
 * 2. 验证令牌有效性
 * 3. 从令牌中提取用户信息
 * <p>
 * 安全特性：
 * - 使用HMAC-SHA256签名算法
 * - 支持BASE64编码的密钥
 * - 包含issuer验证
 * - 完善的异常处理
 *
 * @author zhangabosheng
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String ISSUER = "interview-auth";

    // 从配置文件中注入的JWT密钥（需BASE64编码）
    private String secretKey;
    // 从配置文件中注入的令牌有效期（毫秒）
    private long expiration;

    /**
     * 生成JWT令牌
     *
     * @param username 用户名（令牌主题）
     * @param role     用户角色（将转换为List存储）
     * @return 签名的JWT字符串
     * <p>
     * 令牌结构：
     * - issuer: 固定为"interview-auth"
     * - subject: 用户名
     * - roles: 用户角色列表
     * - issuedAt: 签发时间
     * - expiration: 过期时间
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                // 设置发行者标识
                .setIssuer(ISSUER)
                // 设置主题（通常为用户标识）
                .setSubject(username)
                // 声明角色（使用List保持结构一致性）
                .claim("roles", List.of(role))
                // 设置签发时间
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // 使用HMAC-SHA256签名
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                // 生成紧凑型字符串
                .compact();
    }

    /**
     * 从令牌中提取用户名（subject）
     *
     * @param token JWT令牌字符串
     * @return 用户名
     * @throws JwtException 如果令牌无效或过期
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 从令牌中提取角色列表
     *
     * @param token JWT令牌字符串
     * @return 角色列表（可能为空）
     */
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roleObj = claims.get("roles");
        // 安全类型转换
        if (roleObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) roleObj;
            return roles;
        }
        return List.of();
    }


    /**
     * 验证JWT令牌有效性
     *
     * @param token JWT令牌字符串
     * @return true如果令牌有效，false如果无效或过期
     * <p>
     * 验证内容：
     * 1. 签名有效性
     * 2. 发行者匹配
     * 3. 过期时间
     * 4. 令牌结构完整性
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("JWT已过期", ex);
        } catch (MalformedJwtException ex) {
            logger.error("无效的JWT结构", ex);
        } catch (Exception ex) {
            logger.error("JWT验证失败", ex);
        }
        return false;
    }


    /**
     * 提取令牌中的所有声明(claims)
     *
     * @param token JWT令牌字符串
     * @return Claims对象
     * @throws JwtException 如果令牌无效
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .requireIssuer(ISSUER)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥
     *
     * @return HMAC-SHA密钥对象
     * <p>
     * 密钥处理流程：
     * 1. BASE64解码配置的密钥字符串
     * 2. 转换为HMAC-SHA密钥
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}