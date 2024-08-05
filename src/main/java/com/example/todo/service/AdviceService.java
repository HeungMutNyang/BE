package com.example.todo.service;

import com.example.todo.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdviceService {

    @Autowired
    private UserService userService;

    public String getAdvice(String email) {
        UserEntity userEntity = userService.findByEmail(email);
        if (userEntity == null) {
            return "User not found";
        }

        double height = userEntity.getHeight();
        double weight = userEntity.getWeight();
        double bmi = weight / ((height * height)/10000);

        if (bmi >= 25) {
            return "당신의 bmi 지수는 " + bmi + ". 높아요. 달리기를 하러 가볼까요?";
        } else if (bmi < 18.5) {
            return "당신의 bmi 지수는 " + bmi + ". 낮아요. 먹는양을 늘려 볼까요?";
        } else {
            return "당신의 bmi 지수는 " + bmi + ". 적당해요. 이대로 유지해도 좋아요";
        }
    }
}