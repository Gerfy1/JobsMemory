package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.Notification;
import com.Jobs.JobsMemory.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return this.notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotificationsByUserId(Long userId) {
        return this.notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public Notification createNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return this.notificationRepository.save(notification);
    }

    public Notification markAsRead(Long id) {
        Notification notification = (Notification)this.notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return this.notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        for(Notification notification : this.notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)) {
            notification.setRead(true);
            this.notificationRepository.save(notification);
        }

    }
}
