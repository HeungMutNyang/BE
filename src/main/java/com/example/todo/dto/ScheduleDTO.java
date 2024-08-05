package com.example.todo.dto;

import com.example.todo.model.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleDTO {
    private String id;
    private String title;
    private boolean done = false;
    private LocalDateTime scheduleDateTime; // 날짜와 시간 추가

    public ScheduleDTO(final ScheduleEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
        this.scheduleDateTime = entity.getScheduleDateTime();
    }

    public static ScheduleEntity toEntity(final ScheduleDTO dto) {
        return ScheduleEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .scheduleDateTime(dto.getScheduleDateTime())
                .build();
    }
}



