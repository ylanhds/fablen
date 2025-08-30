package com.cloud.auth.repository;

import com.cloud.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM UserAccount u JOIN u.roles r WHERE u.username = :username")
    Collection<Role> findRolesByGithubLogin(@Param("username") String username);

    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.name = :name")
    boolean existsByName(@Param("name") String name);
}


