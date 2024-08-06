//package com.example.todo.controller;
//
//import com.example.todo.dto.DiaryDTO;
//import com.example.todo.dto.ResponseDTO;
//import com.example.todo.model.DiaryEntity;
//import com.example.todo.service.DiaryService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@RestController
//@RequestMapping("diary")
//public class DiaryController {
//
//    @Autowired
//    private DiaryService service;
//
//    @PostMapping
//    public ResponseEntity<?> createDiary(@AuthenticationPrincipal String userId, @RequestBody DiaryDTO dto) {
//        try {
//            DiaryEntity entity = DiaryDTO.toEntity(dto);
//            entity.setId(null);
//            entity.setUserId(userId);
//            List<DiaryEntity> entities = service.create(entity);
//            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<?> retrieveDiary(@AuthenticationPrincipal String userId) {
//        List<DiaryEntity> entities = service.retrieve(userId);
//        List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
//        ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
//        return ResponseEntity.ok().body(response);
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateDiary(@AuthenticationPrincipal String userId, @RequestBody DiaryDTO dto) {
//        try {
//            DiaryEntity entity = DiaryDTO.toEntity(dto);
//            entity.setUserId(userId);
//            List<DiaryEntity> entities = service.update(entity);
//            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteDiary(@AuthenticationPrincipal String userId, @RequestBody DiaryDTO dto) {
//        try {
//            DiaryEntity entity = DiaryDTO.toEntity(dto);
//            entity.setUserId(userId);
//            List<DiaryEntity> entities = service.delete(entity);
//            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            String error = e.getMessage();
//            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//}

package com.example.todo.controller;

import com.example.todo.dto.DiaryDTO;
import com.example.todo.dto.ResponseDTO;
import com.example.todo.model.DiaryEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.DiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("diary")
public class DiaryController {

    @Autowired
    private DiaryService service;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> createDiary(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            DiaryDTO dto = new ObjectMapper().convertValue(request.get("diary"), DiaryDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userId);
            List<DiaryEntity> entities = service.create(entity);
            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveDiary(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String userId = tokenProvider.validateAndGetUserId(token);

        List<DiaryEntity> entities = service.retrieve(userId);
        List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
        ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateDiary(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            DiaryDTO dto = new ObjectMapper().convertValue(request.get("diary"), DiaryDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setUserId(userId);
            List<DiaryEntity> entities = service.update(entity);
            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiary(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            DiaryDTO dto = new ObjectMapper().convertValue(request.get("diary"), DiaryDTO.class);
            String userId = tokenProvider.validateAndGetUserId(token);

            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setUserId(userId);
            List<DiaryEntity> entities = service.delete(entity);
            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}


