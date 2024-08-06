package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.ScheduleDTO;
import com.example.todo.model.ScheduleEntity;
import com.example.todo.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @PostMapping
    public ResponseEntity<?> createSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
        try {
            log.info("Log:createSchedule entrance");
            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
            log.info("Log:dto => entity ok!");
            entity.setId(null);
            entity.setUserId(userId);
            List<ScheduleEntity> entities = service.create(entity);
            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
            log.info("Log:entities => dtos ok!");
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
            log.info("Log:responsedto ok!");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveSchedule(@AuthenticationPrincipal String userId, @RequestParam String startDateTime, @RequestParam String endDateTime) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        LocalDateTime end = LocalDateTime.parse(endDateTime);
        List<ScheduleEntity> entities = service.retrieveByDateTimeRange(userId, start, end);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/date")
    public ResponseEntity<?> retrieveScheduleByDate(@AuthenticationPrincipal String userId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ScheduleEntity> entities = service.retrieveByDate(userId, localDate);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> retrieveAllSchedules(@AuthenticationPrincipal String userId) {
        List<ScheduleEntity> entities = service.retrieve(userId);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }


    public ResponseEntity<?> retrieve(@AuthenticationPrincipal String userId) {
        List<ScheduleEntity> entities = service.retrieve(userId);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
        try {
            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
            entity.setUserId(userId);
            List<ScheduleEntity> entities = service.update(entity);
            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
        try {
            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
            entity.setUserId(userId);
            List<ScheduleEntity> entities = service.delete(entity);
            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/complete")
    public ResponseEntity<?> completeSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
        try {
            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
            entity.setUserId(userId);
            entity.setDone(true);
            List<ScheduleEntity> entities = service.update(entity);
            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}












