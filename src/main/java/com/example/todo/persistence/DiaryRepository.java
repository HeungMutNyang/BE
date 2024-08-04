package com.example.todo.persistence;

import com.example.todo.model.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, String> {
    List<DiaryEntity> findByUserId(String userId);
}

