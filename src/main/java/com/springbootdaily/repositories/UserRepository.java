package com.springbootdaily.repositories;

import com.springbootdaily.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    @Query(value = "SELECT r.* FROM roles r INNER JOIN users_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId", nativeQuery = true)
    List<Object[]> findRolesByUserId(Long userId);

    @Query(value = "DELETE FROM users_roles us WHERE us.user_id = :userId", nativeQuery = true)
    void deleteAllRolesByUserId(Long userId);
}

