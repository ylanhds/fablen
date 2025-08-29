package com.interview.auth.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangbaosheng
 */
@Setter
@Getter
public class GithubUser {
    // GitHub 用户名
    private String login;
    // GitHub 用户 ID
    @JsonProperty("id")
    private Long githubId;
    // 用户头像 URL
    @JsonProperty("avatar_url")
    private String avatarUrl;
    // 用户邮箱
    private String email;
    // 用户真实姓名
    private String name;

}