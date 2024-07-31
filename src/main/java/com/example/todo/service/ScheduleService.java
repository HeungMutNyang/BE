package com.example.todo.service;

import com.example.todo.model.ScheduleEntity;
import com.example.todo.persistence.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository repository;

    public List<ScheduleEntity> create(final ScheduleEntity entity) {
        validate(entity);
        repository.save(entity);
        return repository.findByUserId(entity.getUserId());
    }

    public List<ScheduleEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<ScheduleEntity> retrieveByDateTimeRange(final String userId, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return repository.findByUserIdAndScheduleDateTimeBetween(userId, startDateTime, endDateTime);
    }

    public List<ScheduleEntity> update(final ScheduleEntity entity) {
        validate(entity);
        if (repository.existsById(entity.getId())) {
            repository.save(entity);
        } else {
            throw new RuntimeException("Unknown id");
        }
        return repository.findByUserId(entity.getUserId());
    }

    public List<ScheduleEntity> delete(final ScheduleEntity entity) {
        if (repository.existsById(entity.getId()))
            repository.deleteById(entity.getId());
        else
            throw new RuntimeException("id does not exist");
        return repository.findByUserId(entity.getUserId());
    }

    public void validate(final ScheduleEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if (entity.getUserId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }
}





