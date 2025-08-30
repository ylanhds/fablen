package com.fablen.auth.service.impl;

import com.fablen.auth.dto.LoginRequest;
import com.fablen.auth.service.AbstractAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2.2 LDAP 登录服务 具体的登录方式
 *
 * @author zhangbaosheng
 */
@RequiredArgsConstructor
@Service("ldapAuthService")
public class LdapAuthService implements AbstractAuthService {

    private static final Logger logger = LoggerFactory.getLogger(LdapAuthService.class);

    private final LdapTemplate ldapTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails authenticate(LoginRequest request) {
        try {
            // 验证 LDAP 用户名和密码
            // 1.搜索路径 如果配置文件设置了 ou=people,dc=example,dc=com 这里要为空字符串，因为我们配置了 base 不然会报错 LDAP: error code 32 - No Such Object
            String baseDn = "";
            // 2.查询过滤器
            String filter = "(cn=" + request.getUsername() + ")";

            logger.debug("Authenticating user with base DN: {}", baseDn);
            logger.debug("Using filter: {}", filter);

            // 3.用户密码
            boolean authenticated = ldapTemplate.authenticate(baseDn, filter, request.getPassword());

            if (!authenticated) {
                throw new BadCredentialsException("Invalid LDAP username or password");
            }

            // 返回 UserDetails（可以创建一个 LDAP 特定的 UserDetails 实现）
            return new org.springframework.security.core.userdetails.User(
                    request.getUsername(),
                    request.getPassword(),
                    // 默认权限
                    List.of(new SimpleGrantedAuthority("ROLE_LDAP_USER"))
            );
        } catch (NameNotFoundException e) {
            logger.error("LDAP user not found: {}", request.getUsername(), e);
            throw new BadCredentialsException("User not found in LDAP", e);
        } catch (Exception e) {
            logger.error("LDAP authentication failed for user: {}", request.getUsername(), e);
            throw new BadCredentialsException("LDAP authentication failed", e);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> loadUserPermissions(UserDetails userDetails) {
        String username = userDetails.getUsername();
        String baseDn = "";

        // 构造查询过滤器
        String filter = "(&(objectClass=groupOfNames)(member=cn=" + username + ",ou=people,dc=example,dc=com))";
        logger.debug("LDAP query filter: {}", filter);

        try {
            // 执行查询
            List<String> groups = ldapTemplate.search(
                    baseDn,
                    // 查询过滤器
                    filter,
                    // 提取组名
                    (AttributesMapper<String>) attributes -> attributes.get("cn").get().toString()
            );

            logger.debug("LDAP groups found for user {}: {}", username, groups);
            // 将组信息映射为权限
            return groups.stream()
                    .map(group -> new SimpleGrantedAuthority("ROLE_" + group.toUpperCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching LDAP groups for user {}", username, e);
            throw new RuntimeException("Failed to load user permissions from LDAP", e);
        }
    }
}
