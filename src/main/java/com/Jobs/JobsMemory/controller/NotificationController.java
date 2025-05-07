package com.Jobs.JobsMemory.controller;


import com.Jobs.JobsMemory.model.Notification;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.service.NotificationService;
import com.Jobs.JobsMemory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    private com.Jobs.JobsMemory.model.User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = null;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            if (username != null) {
                try {
                    return userService.findByUsername(username);
                } catch (UsernameNotFoundException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications() {
        User currentUser = getCurrentUser();
        if (currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Notification> notifications = notificationService.getNotificationsForUser(currentUser);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id){
        User currentUser = getCurrentUser();
        if (currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Notification> updateNotification = notificationService.markAsRead(id, currentUser);
        return updateNotification
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markNotificationAsReadAll(){
        User currentUser = getCurrentUser();
        if (currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        notificationService.markAllAsReadForUser(currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id){
        User currentUser = getCurrentUser();
        if (currentUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean deleted = notificationService.deleteNotification(id, currentUser);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllUserNotifications() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        notificationService.deleteAllNotificationsForUser(currentUser);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
