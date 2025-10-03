package com.example.Qolzy.repository;

import com.example.Qolzy.model.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByFirstNameAndLastNameContainingIgnoreCase(String firstName, String lastName);
    UserEntity findUserByEmail(String email);
    UserEntity findUserByPhone(String phone);
    UserEntity findUserById(Long id);
    UserEntity findUserByProviderId(String providerId);
    UserEntity findUserByUserName(String userName);
    boolean existsByUserName(String userName);
}
