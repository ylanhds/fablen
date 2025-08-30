package com.cloud.auth.config;

import com.fablen.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 需求（GitHub 登录要用 GithubAuthService 实现，支持多方式登录，Controller 端统一登录接口），
     * 适用于前后端分离的完整 SecurityConfig 配置，不使用 Spring Security 默认的OAuth2登录跳转流程，
     * 而是全部通过接口和自定义认证逻辑处理（即通过 /api/login 或 /api/login/github），避免页面重定向，适合你现在的业务结构。
     * <p>
     * 说明：
     * <p>
     * 本配置假设所有认证都通过 JWT，并用你自定义的 JwtAuthenticationFilter 做认证。
     * GitHub 登录交给 Controller 和 GithubAuthService，不走Spring Security的oauth2Login()机制。
     * 放行登录接口、GitHub回调、静态资源等路径。
     * 关闭CSRF，配置CORS，所有其它接口都要JWT认证。
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭session，前后端分离无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 放行登录、GitHub回调、静态资源、健康检查等
                        .requestMatchers(
                                "/api/login",
                                "/api/login/github",
                                "/oauth2/**",
                                "/login/oauth2/code/**",
                                "/favicon.ico",
                                "/error",
                                "/"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 其余接口需要认证
                        .anyRequest().authenticated()
                );

        // JWT认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    // CORS 配置
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        // 允许你前端地址
//        config.addAllowedOrigin("*");
//        // 跨域允许带cookie
//        config.setAllowCredentials(true);
//        // 兼容所有源
//        config.addAllowedOriginPattern("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.addExposedHeader("Authorization");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}