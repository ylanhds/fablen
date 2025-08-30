package com.fablen.auth.service.impl;

import com.fablen.auth.client.GithubApiClient;
import com.fablen.auth.dto.LoginRequest;
import com.fablen.auth.service.AbstractAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;
import java.util.List;

/**
 * 2.3 GitHub 登录服务 具体的登录方式
 *
 * @author zhangbaosheng
 */
@RequiredArgsConstructor
@Service("githubAuthService")
public class GithubAuthService implements AbstractAuthService {
    private static final Logger logger = LoggerFactory.getLogger(GithubAuthService.class);

    private final GithubApiClient githubApiClient;

    @Override
    public UserDetails authenticate(LoginRequest request) {
        try {  // 1. 请求 GitHub 换取 access_token
//            logger.info("开始GitHub认证流程 - code: {}, state: {}", request.getCode(), request.getState());
//            // 1. 验证state参数（防止CSRF攻击）
//            if (!StringUtils.hasText(request.getState())) {
//                logger.error("缺少state参数，可能遭受CSRF攻击");
//                throw new BadCredentialsException("Invalid state parameter");
//            }
//            // 2. 获取access_token
//            String response = githubApiClient.getCodeResponse(request.getCode(), request.getState());
//            logger.debug("GitHub token响应: {}", response);
//
//            // 3. 解析access_token
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonNode = mapper.readTree(response);
//
//            // 增加错误响应处理
//            if (jsonNode.has("error")) {
//                String error = jsonNode.get("error").asText();
//                logger.error("GitHub返回错误: {}", error);
//                throw new BadCredentialsException("GitHub error: " + error);
//            }
//
//            String accessToken = jsonNode.path("access_token").asText();
//            if (!StringUtils.hasText(accessToken)) {
//                throw new BadCredentialsException("无效的access_token");
//            }
//
//            // 4. 获取用户信息
//            GithubUser githubUser = githubApiClient.getUserInfo(accessToken);
//            logger.info("GitHub用户登录成功: {}", githubUser.getLogin());
//
//            return new org.springframework.security.core.userdetails.User(
//                    githubUser.getLogin(),
                    // 密码留空，因为使用OAuth2
//                    "",
//                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
//            );
//            TODO  github服务 连不上

            return new org.springframework.security.core.userdetails.User(
                    "",
                    "",
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (WebClientResponseException e) {
            logger.error("GitHub API请求失败 - 状态码: {}, 响应: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new BadCredentialsException("GitHub服务不可用", e);
        } catch (Exception e) {
            logger.error("GitHub认证过程异常", e);
            throw new RuntimeException("认证服务异常", e);
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> loadUserPermissions(UserDetails userDetails) {
        // GitHub 用户的权限可以从数据库或配置文件中加载
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
