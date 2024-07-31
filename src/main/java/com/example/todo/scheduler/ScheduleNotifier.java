package com.example.todo.scheduler;

import com.example.todo.model.ScheduleEntity;
import com.example.todo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleNotifier {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 60000) // 매 1분마다 실행
    public void notifySchedules() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMinute = now.withSecond(0).withNano(0);
        LocalDateTime endOfMinute = now.withSecond(59).withNano(999999999);

        List<ScheduleEntity> schedules = scheduleService.retrieveByDateTimeRange("user123", startOfMinute, endOfMinute);

        for (ScheduleEntity schedule : schedules) {
            if (!schedule.isDone()) {
                messagingTemplate.convertAndSend("/topic/schedule", schedule);
            }
        }
    }
}



