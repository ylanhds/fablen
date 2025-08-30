package com.cloud.auth.repository;

import com.cloud.auth.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author zhangbaosheng
 */
@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    // 通过用户名查找用户（区分大小写）
    @Query("SELECT u FROM UserAccount u WHERE u.username = ?1")
    Optional<UserAccount> findByUsername(String username);
}