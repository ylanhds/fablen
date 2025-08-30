package com.fablen.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fablen.auth.entity.UserAccount;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author zhangbaosheng
 */
@Repository
public interface UserMapper extends BaseMapper<UserAccount> {
    // 通过用户名查找用户（区分大小写）
    @Select("SELECT u FROM UserAccount u WHERE u.username = ?1")
    Optional<UserAccount> findByUsername(String username);
}