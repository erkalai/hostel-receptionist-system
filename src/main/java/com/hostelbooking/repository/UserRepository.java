package com.hostelbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hostelbooking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    
//    Optional<User> findByUsername(String email);
    
    @Modifying
    @Query(value = "UPDATE users SET name = :name, email = :email, password = :password WHERE id = :id", nativeQuery = true)
    int updateUserNative(@Param("id") Long id,
                         @Param("name") String name,
                         @Param("email") String email,
                         @Param("password") String password);

    // Delete user's current roles
    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE user_id = :userId", nativeQuery = true)
    int deleteUserRoles(@Param("userId") Long userId);

    // Add a new role for the user
    @Modifying
    @Query(value = "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    int addUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
//    User findById(Long id);
//    Optional<User> findByEmail(String email);
//	User findByUsername(String username);
}