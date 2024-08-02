package com.example.todo.persistence;

import com.example.todo.model.ScheduleCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleCheckRepository extends JpaRepository<ScheduleCheckEntity, Long> {

    List<ScheduleCheckEntity> findByUserId(String userId);

}

