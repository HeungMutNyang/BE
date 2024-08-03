package com.example.todo.service;

import com.example.todo.model.DiaryEntity;
import com.example.todo.persistence.DiaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DiaryService {

    @Autowired
    private DiaryRepository repository;

    public List<DiaryEntity> create(final DiaryEntity entity) {
        validate(entity);
        entity.setCreatedDate(LocalDateTime.now());
        repository.save(entity);
        return repository.findByUserId(entity.getUserId());
    }

    public List<DiaryEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<DiaryEntity> update(final DiaryEntity entity) {
        validate(entity);
        if (repository.existsById(entity.getId())) {
            entity.setUpdatedDate(LocalDateTime.now());
            repository.save(entity);
        } else {
            throw new RuntimeException("Unknown id");
        }
        return repository.findByUserId(entity.getUserId());
    }

    public List<DiaryEntity> delete(final DiaryEntity entity) {
        if (repository.existsById(entity.getId())) {
            repository.deleteById(entity.getId());
        } else {
            throw new RuntimeException("id does not exist");
        }
        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final DiaryEntity entity) {
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
