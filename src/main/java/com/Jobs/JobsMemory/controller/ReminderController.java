package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.Reminder;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.service.JobApplicationService;
import com.Jobs.JobsMemory.service.ReminderService;
import com.Jobs.JobsMemory.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/reminders"})
public class ReminderController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ReminderController.class);
    private final ReminderService reminderService;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public ReminderController(ReminderService reminderService, UserService userService, JobApplicationService jobApplicationService) {
        this.reminderService = reminderService;
        this.userService = userService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<Reminder>> getAllReminders() {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Reminder> reminders = this.reminderService.getAllRemindersByUserId(currentUser.getId());
            return ResponseEntity.ok(reminders);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Reminder> getReminderById(@PathVariable long id) {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Optional<Reminder> reminder = this.reminderService.getReminderById(id);
            return reminder.isPresent() && reminder.get().getJobApplication().getUser().getId().equals(currentUser.getId()) ? ResponseEntity.ok((Reminder)reminder.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping({"/job-application/{jobApplicationId}"})
    public ResponseEntity<List<Reminder>> getRemindersByJobApplicationId(@PathVariable long jobApplicationId) {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Optional<JobApplication> jobApp = this.jobApplicationService.getJobApplicationById(jobApplicationId);
            if (jobApp.isPresent() && jobApp.get().getUser().getId().equals(currentUser.getId())) {
                List<Reminder> reminders = this.reminderService.getRemindersByJobApplicationId(jobApplicationId);
                return ResponseEntity.ok(reminders);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @GetMapping({"/upcoming"})
    public ResponseEntity<List<Reminder>> getUpcomingReminders() {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Reminder> reminders = this.reminderService.getUpcomingReminders(currentUser.getId());
            return ResponseEntity.ok(reminders);
        }
    }

    @PostMapping
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminderRequest) {
        log.info("Recebendo novo lembrete: {}", reminderRequest);
        log.info("Data recebida: {}", reminderRequest.getDate());
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Long jobApplicationId = reminderRequest.getJobApplicationId();
            if (jobApplicationId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                Optional<JobApplication> jobAppOptional = this.jobApplicationService.getJobApplicationById(jobApplicationId);
                if (jobAppOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                } else {
                    JobApplication jobApp = (JobApplication)jobAppOptional.get();
                    if (!jobApp.getUser().getId().equals(currentUser.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    } else {
                        Reminder reminder = new Reminder();
                        reminder.setTitle(reminderRequest.getTitle());
                        reminder.setDescription(reminderRequest.getDescription());
                        reminder.setDate(reminderRequest.getDate());
                        reminder.setCompleted(false);
                        reminder.setJobApplication(jobApp);
                        Reminder savedReminder = this.reminderService.saveReminder(reminder);
                        return ResponseEntity.status(HttpStatus.CREATED).body(savedReminder);
                    }
                }
            }
        }
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Reminder> updateReminder(@PathVariable Long id, @RequestBody Reminder reminder) {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Optional<Reminder> existingReminderOpt = this.reminderService.getReminderById(id);
            if (existingReminderOpt.isPresent()) {
                Reminder existingReminder = (Reminder)existingReminderOpt.get();
                if (existingReminder.getJobApplication().getUser().getId().equals(currentUser.getId())) {
                    reminder.setId(id);
                    reminder.setJobApplication(existingReminder.getJobApplication());
                    Reminder updatedReminder = this.reminderService.saveReminder(reminder);
                    return ResponseEntity.ok(updatedReminder);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PutMapping({"/{id}/toggle-completion"})
    public ResponseEntity<Boolean> toggleReminderCompletion(@PathVariable Long id) {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Optional<Reminder> reminderOpt = this.reminderService.getReminderById(id);
            if (reminderOpt.isPresent() && ((Reminder)reminderOpt.get()).getJobApplication().getUser().getId().equals(currentUser.getId())) {
                boolean isCompleted = this.reminderService.toggleReminderCompletion(id);
                return ResponseEntity.ok(isCompleted);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        User currentUser = this.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Optional<Reminder> reminderOpt = this.reminderService.getReminderById(id);
            if (reminderOpt.isPresent() && ((Reminder)reminderOpt.get()).getJobApplication().getUser().getId().equals(currentUser.getId())) {
                this.reminderService.deleteReminder(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            return this.userService.findByUsername(userDetails.getUsername());
        } else {
            return null;
        }
    }
}
