package com.example.ws.io.repository;

import com.example.ws.io.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);
}
