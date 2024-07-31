package com.example.todo.persistence;

import com.example.todo.model.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {

    @Query("select s from ScheduleEntity s where s.userId = ?1")
    List<ScheduleEntity> findByUserId(String userId);

    @Query("select s from ScheduleEntity s where s.userId = ?1 and s.scheduleDateTime between ?2 and ?3")
    List<ScheduleEntity> findByUserIdAndScheduleDateTimeBetween(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}



