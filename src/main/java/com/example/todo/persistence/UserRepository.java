package com.example.todo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    ///추가
  	Optional<UserEntity> findById(String Id);
  	///추가
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);
}
