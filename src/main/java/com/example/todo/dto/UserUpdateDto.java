package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {
    private String id;
    private String username;
    private String email;
    private Double height;  // 추가
    private Double weight;  // 추가
}
