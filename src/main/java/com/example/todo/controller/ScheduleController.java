//package com.example.todo.controller;
//
//import com.example.todo.dto.ResponseDTO;
//import com.example.todo.dto.ScheduleDTO;
//import com.example.todo.model.ScheduleEntity;
//import com.example.todo.service.ScheduleService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@RestController
//@RequestMapping("schedule")
//public class ScheduleController {
//
//    @Autowired
//    private ScheduleService service;
//
//    @PostMapping
//    public ResponseEntity<?> createSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
//        try {
//            log.info("Log:createSchedule entrance");
//            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
//            log.info("Log:dto => entity ok!");
//            entity.setId(null);
//            entity.setUserId(userId);
//            entity.setDone(false);
//            List<ScheduleEntity> entities = service.create(entity);
//            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//            log.info("Log:entities => dtos ok!");
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//            log.info("Log:responsedto ok!");
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<?> retrieveSchedule(@AuthenticationPrincipal String userId, @RequestParam String startDateTime, @RequestParam String endDateTime) {
//        LocalDateTime start = LocalDateTime.parse(startDateTime);
//        LocalDateTime end = LocalDateTime.parse(endDateTime);
//        List<ScheduleEntity> entities = service.retrieveByDateTimeRange(userId, start, end);
//        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//
//        return ResponseEntity.ok().body(response);
//    }
//
//    @GetMapping("/date")
//    public ResponseEntity<?> retrieveScheduleByDate(@AuthenticationPrincipal String userId, @RequestParam String date) {
//        LocalDate localDate = LocalDate.parse(date);
//        List<ScheduleEntity> entities = service.retrieveByDate(userId, localDate);
//        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//        return ResponseEntity.ok().body(response);
//    }
//
//
//    @PutMapping
//    public ResponseEntity<?> updateSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
//        try {
//            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
//            entity.setUserId(userId);
//            List<ScheduleEntity> entities = service.update(entity);
//            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
//        try {
//            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
//            entity.setUserId(userId);
//            List<ScheduleEntity> entities = service.delete(entity);
//            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PutMapping("/complete")
//    public ResponseEntity<?> completeSchedule(@AuthenticationPrincipal String userId, @RequestBody ScheduleDTO dto) {
//        try {
//            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
//            entity.setUserId(userId);
//            entity.setDone(true);
//            List<ScheduleEntity> entities = service.update(entity);
//            List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//}

package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.ScheduleDTO;
import com.example.todo.model.ScheduleEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody Map<String, Object> request) {
        try {
            log.info("Log:createSchedule entrance");
            String token = (String) request.get("token");
            ScheduleDTO dto = new ObjectMapper().convertValue(request.get("schedule"), ScheduleDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

            ScheduleEntity entity = ScheduleDTO.toEntity(dto);
            log.info("Log:dto => entity ok!");
            entity.setId(null);
            entity.setUserId(userId);
            entity.setDone(false);
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
    public ResponseEntity<?> retrieveSchedule(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String userId = tokenProvider.validateAndGetUserId(token);
        String startDateTime = request.get("startDateTime");
        String endDateTime = request.get("endDateTime");
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        LocalDateTime end = LocalDateTime.parse(endDateTime);

        List<ScheduleEntity> entities = service.retrieveByDateTimeRange(userId, start, end);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/date")
    public ResponseEntity<?> retrieveScheduleByDate(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String userId = tokenProvider.validateAndGetUserId(token);
        String date = request.get("date");
        LocalDate localDate = LocalDate.parse(date);

        List<ScheduleEntity> entities = service.retrieveByDate(userId, localDate);
        List<ScheduleDTO> dtos = entities.stream().map(ScheduleDTO::new).collect(Collectors.toList());
        ResponseDTO<ScheduleDTO> response = ResponseDTO.<ScheduleDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            ScheduleDTO dto = new ObjectMapper().convertValue(request.get("schedule"), ScheduleDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

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
    public ResponseEntity<?> deleteSchedule(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            ScheduleDTO dto = new ObjectMapper().convertValue(request.get("schedule"), ScheduleDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

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
    public ResponseEntity<?> completeSchedule(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            ScheduleDTO dto = new ObjectMapper().convertValue(request.get("schedule"), ScheduleDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

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










