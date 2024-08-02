package com.example.todo.dto;


import com.example.todo.model.ScheduleCheckEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleCheckDTO {

    private String scheduleId;
    private String userId;
    private String scheduleTitle;
    private Boolean done;


    public static ScheduleCheckEntity DTOtoENTITY(ScheduleCheckDTO scheduleCheckDTO){
        return ScheduleCheckEntity.builder()
                .id(scheduleCheckDTO.getScheduleId())
                .userId(scheduleCheckDTO.getUserId())
                .done(scheduleCheckDTO.getDone())
                .build();
    }

    public static ScheduleCheckDTO ENTITYtoDTO(ScheduleCheckEntity scheduleCheckEntity){
        return ScheduleCheckDTO.builder()
                .scheduleId(scheduleCheckEntity.getId())
                .userId(scheduleCheckEntity.getUserId())
                .done(scheduleCheckEntity.isDone())
                .build();
    }
}

