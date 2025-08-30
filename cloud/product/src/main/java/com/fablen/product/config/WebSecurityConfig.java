package com.fablen.product.config;

import com.fablen.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 核心配置类
 * 1. 配置JWT认证过滤器
 * 2. 定义安全策略（路由权限、CORS、CSRF等）
 * 3. 配置无状态会话管理
 *
 * @author zhangbaosheng
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 启用Spring Security
@EnableMethodSecurity // 启用方法级安全控制（如@PreAuthorize）
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 安全过滤器链配置
     *
     * @param http HttpSecurity配置构建器
     * @return 配置完成的安全过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 添加JWT认证过滤器到UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                // 禁用CSRF保护（因使用JWT无状态认证）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置CORS跨域策略
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/products").permitAll() // 产品API允许匿名访问
                        .anyRequest().authenticated() // 其他所有请求需要认证
                )

                // 配置无状态会话（不使用HTTP Session）
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 认证异常处理（返回401状态码）
                .exceptionHandling(e -> e.authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }));

        return http.build();
    }

//    /**
//     * CORS跨域配置源
//     *
//     * @return 自定义的CORS配置
//     */
//    private CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true); // 允许携带凭证（如cookies）
//            config.addAllowedOrigin("*"); // 允许的前端地址
//            config.addAllowedHeader("*"); // 允许所有请求头
//            config.addAllowedMethod("*"); // 允许所有HTTP方法
//            config.addExposedHeader("Authorization"); // 暴露Authorization头
//            return config;
//        };
//    }
}