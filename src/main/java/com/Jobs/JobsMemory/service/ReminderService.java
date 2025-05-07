package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.Reminder;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.ReminderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ReminderService {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private final ReminderRepository reminderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private final JobApplicationService jobApplicationService;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository,
                           JobApplicationService jobApplicationService,
                           NotificationService notificationService,
                           UserService userService) {
        this.reminderRepository = reminderRepository;
        this.jobApplicationService = jobApplicationService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.findByUsername(userDetails.getUsername());
        }
        return null;
    }

    private Long getAuthenticateUserId() {
        User user = getCurrentUser();
        return (user != null) ? user.getId() : null;
    }

    public List<Reminder> getAllRemindersForCurrentUser(){
        Long userId= getAuthenticateUserId();
        if (userId == null){
            return Collections.emptyList();
        }
        return this.reminderRepository.findByJobApplication_User_Id(userId);
    }

    public List<Reminder> getUpcomingRemindersForCurrentUser(){
        Long userId = getAuthenticateUserId();
        if (userId == null){
            return Collections.emptyList();
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekLater = now.plusDays(7L);

        return this.reminderRepository.findUpcomingReminders(userId, now, weekLater);
    }

    public List<Reminder> getAllRemindersByUserId(Long userId) {
        return this.reminderRepository.findByJobApplication_User_Id(userId);
    }

    public List<Reminder> getRemindersByJobApplicationId(Long jobApplicationId) {
        return this.reminderRepository.findByJobApplication_Id(jobApplicationId);
    }

    public Optional<Reminder> getReminderById(Long reminderId) {
        Long userId = getAuthenticateUserId();
        if (userId == null) {
            return Optional.empty();
        }
        Optional<Reminder> reminderOpt = this.reminderRepository.findById(reminderId);
        if (reminderOpt.isPresent() && !reminderOpt.get().getJobApplication().getUser().getId().equals(userId)){
            System.err.println("ReminderService: Tentativa de acesso não autorizado ao lembrete com ID: " + reminderId + " Por usuario: "+ userId);
            return Optional.empty();
        }
        return reminderOpt;
    }

    public List<Reminder> getUpcomingReminders(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekLater = now.plusDays(7L);
        return this.reminderRepository.findUpcomingReminders(userId, now, weekLater);
    }

    public Reminder saveReminder(Reminder reminder) {
        return this.reminderRepository.save(reminder);
    }

    public void deleteReminder(Long reminderId) {
        Long userId = getAuthenticateUserId();
        if (userId == null) {
            System.err.println("Tentativa de exclusão por usuario não autenticado");
            return;
        }
        Optional<Reminder> reminderOpt = this.reminderRepository.findById(reminderId);
        if (reminderOpt.isPresent()) {
            if (reminderOpt.get().getJobApplication().getUser().getId().equals(userId)) {
                this.reminderRepository.deleteById(reminderId);
            } else {
                System.err.println("Tentativa de exclusão não autorizada do lembrete com ID: " + reminderId + "Por usuario: " + userId);
            }
        } else {
            System.err.println("Tentativa de exclusão de lembrete inexistente com ID: " + reminderId);
        }
    }

    public boolean toggleReminderCompletion(Long reminderId) {
        Optional<Reminder> reminderOpt = this.reminderRepository.findById(reminderId);
        if (reminderOpt.isPresent()) {
            Reminder reminder = reminderOpt.get();
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

        List<Reminder> upcoming = this.reminderRepository.findAllUpcomingReminders(now, tomorrow);

        for(Reminder reminder : upcoming) {
            if (!reminder.isCompleted() && reminder.getJobApplication() != null && reminder.getJobApplication().getUser() != null) {
                JobApplication jobApp = reminder.getJobApplication();
                User user = jobApp.getUser();
                String message = String.format("Lembrete para '%s': %s", jobApp.getJobName(), reminder.getTitle());
                this.notificationService.createNotification(user, message, "reminder", reminder.getId());
            }
        }
    }
}
