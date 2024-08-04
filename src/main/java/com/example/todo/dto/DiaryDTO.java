package com.example.todo.dto;

import com.example.todo.model.DiaryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiaryDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public DiaryDTO(final DiaryEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
    }

    public static DiaryEntity toEntity(final DiaryDTO dto) {
        return DiaryEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdDate(dto.getCreatedDate())
                .updatedDate(dto.getUpdatedDate())
                .build();
    }
}

