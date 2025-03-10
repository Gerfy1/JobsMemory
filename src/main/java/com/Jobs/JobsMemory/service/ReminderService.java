package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.Reminder;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.ReminderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    @Autowired
    private NotificationService notificationService;
    private final ReminderRepository reminderRepository;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, JobApplicationService jobApplicationService) {
        this.reminderRepository = reminderRepository;
        this.jobApplicationService = jobApplicationService;
    }

    public List<Reminder> getAllRemindersByUserId(Long userId) {
        return this.reminderRepository.findByJobApplication_User_Id(userId);
    }

    public List<Reminder> getRemindersByJobApplicationId(Long jobApplicationId) {
        return this.reminderRepository.findByJobApplication_Id(jobApplicationId);
    }

    public Optional<Reminder> getReminderById(Long reminderId) {
        return this.reminderRepository.findById(reminderId);
    }

    public List<Reminder> getUpcomingReminders(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekLater = now.plusDays(7L);
        return this.reminderRepository.findUpcomingReminders(userId, now, weekLater);
    }

    public Reminder saveReminder(Reminder reminder) {
        return (Reminder)this.reminderRepository.save(reminder);
    }

    public void deleteReminder(Long reminderId) {
        this.reminderRepository.deleteById(reminderId);
    }

    public boolean toggleReminderCompletion(Long reminderId) {
        Optional<Reminder> reminderOpt = this.reminderRepository.findById(reminderId);
        if (reminderOpt.isPresent()) {
            Reminder reminder = (Reminder)reminderOpt.get();
            reminder.setCompleted(!reminder.isCompleted());
            this.reminderRepository.save(reminder);
            return reminder.isCompleted();
        } else {
            return false;
        }
    }

    @Transactional
    public void checkAndCreateNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1L);

        for(Reminder reminder : this.reminderRepository.findAllUpcomingReminders(now, tomorrow)) {
            if (!reminder.isCompleted()) {
                JobApplication jobApp = reminder.getJobApplication();
                User user = jobApp.getUser();
                String message = String.format("Lembrete para '%s': %s", jobApp.getJobName(), reminder.getTitle());
                this.notificationService.createNotification(user.getId(), message);
            }
        }

    }
}
