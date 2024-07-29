package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private Integer height;  // 추가
    private Integer weight;  // 추가
}
