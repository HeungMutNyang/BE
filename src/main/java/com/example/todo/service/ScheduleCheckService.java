package com.example.todo.service;

import com.example.todo.model.ScheduleCheckEntity;
import com.example.todo.persistence.ScheduleCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleCheckService {

    @Autowired
    private ScheduleCheckRepository schedulerepository;

    public ScheduleCheckEntity create(ScheduleCheckEntity entity) {
        return schedulerepository.save(entity);
    }

    public List<ScheduleCheckEntity> searchByUserId(String userId) {
        return schedulerepository.findByUserId(userId);
    }

    public ScheduleCheckEntity update(ScheduleCheckEntity entity) {
        return schedulerepository.save(entity);
    }

    public void delete(String id) {
        schedulerepository.deleteById(Long.valueOf(id));
    }
}

