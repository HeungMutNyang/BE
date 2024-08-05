package com.example.todo.controller;

import com.example.todo.security.CustomUserDetails;
import com.example.todo.service.AdviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "advice")
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @GetMapping("/advice")
    public String getAdvice() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();

        return adviceService.getAdvice(email);
    }
}