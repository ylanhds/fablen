package com.fablen.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fablen.auth.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT r FROM UserAccount u JOIN u.roles r WHERE u.username = :username")
    Collection<Role> findRolesByGithubLogin(@Param("username") String username);

    @Select("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(@Param("name") String name);

    @Select("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.name = :name")
    boolean existsByName(@Param("name") String name);
}


