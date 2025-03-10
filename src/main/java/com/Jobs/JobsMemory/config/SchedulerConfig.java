package com.Jobs.JobsMemory.config;

import com.Jobs.JobsMemory.service.ReminderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    private final ReminderService reminderService;

    @Autowired
    public SchedulerConfig(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Scheduled(
            fixedRate = 3600000L
    )
    @Transactional
    public void checkReminders() {
        this.reminderService.checkAndCreateNotifications();
    }
}
