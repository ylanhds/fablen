package com.interview.auth.service.impl;

import com.interview.auth.dto.LoginRequest;
import com.interview.auth.entity.Role;
import com.interview.auth.repository.UserRepository;
import com.interview.auth.service.AbstractAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2.1 数据库登录服务 具体的登录方式
 *
 * @author zhangbaosheng
 */
@RequiredArgsConstructor
@Service("databaseAuthService")
public class DatabaseAuthService implements AbstractAuthService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseAuthService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails authenticate(LoginRequest request) {

        logger.debug("尝试加载用户: {}", request.getUsername());
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    logger.debug("找到用户: {}, 启用状态: {}", user.getUsername(), user.isEnabled());
                    return new User(
                            user.getUsername(),
                            user.getPassword(),
                            user.isEnabled(),
                            true,
                            true,
                            true,
                            mapRolesToAuthorities(user.getRoles())
                    );
                })
                .orElseThrow(() -> {
                    logger.error("用户不存在: {}", request.getUsername());
                    return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public Collection<? extends GrantedAuthority> loadUserPermissions(UserDetails userDetails) {
        // 数据库用户的权限已经在 UserDetails 中
        if (userDetails != null) {
            return userDetails.getAuthorities();
        }
        return List.of();
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
