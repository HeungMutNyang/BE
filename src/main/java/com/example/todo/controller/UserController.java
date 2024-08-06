package com.example.todo.controller;

import java.util.Map;

import com.example.todo.dto.LoginDTO;
import com.example.todo.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "UserController")
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("회원가입 중 오류가 발생했습니다.").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Operation(summary = "Authenticate a user")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .token(token)
                    .build();

//            final LoginDTO responseUserDTO = LoginDTO.builder()
//                    .email(user.getEmail())
//                    .password(user.getPassword())
//                    .build();

            // 헤더에 토큰 추가
            //HttpHeaders headers = new HttpHeaders();
            //headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Operation(summary = "Delete a user account")
    @PostMapping("/delaccount")
    public ResponseEntity<?> deleteAccount(@RequestBody UserDTO userDTO) {
        try {
            String userId = userDTO.getId();
            if (userId == null) {
                log.error("User ID must not be null");
                throw new IllegalArgumentException("User ID must not be null");
            }
            userService.delete(userId);
            return ResponseEntity.ok().body(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting account: ", e);
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Operation(summary = "Get user information")
    @PostMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            if (token == null) {
                throw new IllegalArgumentException("Token must not be null");
            }

            String userId = tokenProvider.validateAndGetUserId(token);
            if (userId == null) {
                throw new IllegalArgumentException("Invalid token");
            }

            UserEntity user = userService.getById(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Operation(summary = "Update user information")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        try {
            if (userUpdateDto.getId() == null) {
                throw new IllegalArgumentException("User ID must not be null");
            }
            UserEntity updatedUser = userService.updateUser(userUpdateDto);
            UserUpdateDto responseUserDTO = UserUpdateDto.builder()
                    .email(updatedUser.getEmail())
                    .id(updatedUser.getId())
                    .username(updatedUser.getUsername())
                    .height(updatedUser.getHeight())
                    .weight(updatedUser.getWeight())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error("사용자 정보 업데이트 중 오류가 발생했습니다.").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
