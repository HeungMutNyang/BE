package com.example.todo.controller;


import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.ScheduleCheckDTO;
import com.example.todo.model.ScheduleCheckEntity;
import com.example.todo.service.ScheduleCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
@Slf4j(topic = "ScheduleCheckController")
public class ScheduleCheckController {

    @Autowired
    private ScheduleCheckService scheduleCheckService;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCheckDTO scheduleCheckDTO, Authentication authentication) {
        try{
            String userId = authentication.getName();
            scheduleCheckDTO.setUserId(userId);

            ScheduleCheckEntity scheduleCheckEntity = ScheduleCheckDTO.DTOtoENTITY(scheduleCheckDTO);
            ScheduleCheckEntity createdEntity = scheduleCheckService.create(scheduleCheckEntity);
            ScheduleCheckDTO responseDTO = ScheduleCheckDTO.ENTITYtoDTO(createdEntity);

            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.error("일정 생성 오류", e);
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("일정 생성 오류").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(Authentication authentication) { //조회
        try{
            String userId = authentication.getName();
            List<ScheduleCheckEntity> schedules = scheduleCheckService.searchByUserId(userId);

            List<ScheduleCheckDTO> responseDTOs = schedules.stream()
                    .map(ScheduleCheckDTO::ENTITYtoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(responseDTOs);
        }catch (Exception e){
            log.error("일정 조회 오류", e);
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("일정 조회 오류").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleCheckDTO scheduleCheckDTO, Authentication authentication) {
        try {
            String userId = authentication.getName();
            scheduleCheckDTO.setUserId(userId);
            scheduleCheckDTO.setScheduleId(scheduleCheckDTO.getScheduleId());

            ScheduleCheckEntity scheduleCheckEntity = ScheduleCheckDTO.DTOtoENTITY(scheduleCheckDTO);
            ScheduleCheckEntity updatedEntity = scheduleCheckService.update(scheduleCheckEntity);
            ScheduleCheckDTO responseDTO = ScheduleCheckDTO.ENTITYtoDTO(updatedEntity);

            return ResponseEntity.ok(responseDTO);
        }catch (Exception e){
            log.error("일정 변경 오류", e);
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("일정 변경 오류").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deleteSchedule(@RequestBody ScheduleCheckDTO scheduleCheckDTO) {
        try{
            if (scheduleCheckDTO.getScheduleId() == null) {
                return ResponseEntity.badRequest().body("삭제할 일정ID가 맞지 않습니다.");
            } else if (scheduleCheckDTO.getScheduleId().isEmpty()) {
                return ResponseEntity.badRequest().body("삭제할 일정이 없습니다.");
            }
            scheduleCheckService.delete(scheduleCheckDTO.getScheduleId());
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            log.error("일정 삭제 오류", e);
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("일정 삭제 오류").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
