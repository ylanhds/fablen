package com.fablen.auth.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

/**
 * @author zhangbaosheng
 */
@Component
public class GithubApiClient {
    private static final Logger logger = LoggerFactory.getLogger(GithubApiClient.class);
    // 增加重试机制和超时设置
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;


    // GitHub API 的基础 URL
    private static final String GITHUB_API_BASE_URL = "https://github.com";

    public GithubApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(GITHUB_API_BASE_URL).build();
    }

    /**
     * 获取 GitHub 用户信息
     *
     * @param accessToken OAuth2 Access Token
     * @return GithubUser 用户信息
     */
    public GithubUser getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/user")
                .headers(headers -> {
                    headers.setBearerAuth(accessToken);
                    headers.set("User-Agent", "InterviewAuthApp"); // GitHub要求User-Agent
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error("GitHub客户端错误: {}", response.statusCode());
                    return Mono.error(new RuntimeException("客户端请求错误"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error("GitHub服务端错误: {}", response.statusCode());
                    return Mono.error(new RuntimeException("GitHub服务暂时不可用"));
                })
                .bodyToMono(GithubUser.class)
                .timeout(TIMEOUT)
                // 重试2次
                .retryWhen(Retry.backoff(2, Duration.ofMillis(100)))
                .block();
    }

    public String getCodeResponse(String code,String state) {
        String response = webClient.post()
                .uri("/login/oauth/access_token")
                .header("Accept", "application/json")
                .header("User-Agent", "InterviewAuthApp")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "code", code,
                        "redirect_uri", redirectUri,
                        "state", state
                ))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(TIMEOUT)
                .block();
        logger.info("GitHub Token 响应: {}", response);
        return response;
    }

}