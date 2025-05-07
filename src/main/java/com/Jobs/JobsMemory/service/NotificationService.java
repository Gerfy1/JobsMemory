package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.Notification;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.Jobs.JobsMemory.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getNotificationsForUser(User user) {
        if (user == null) return List.of();
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Notification createNotification(User user, String message, String type, Long relatedId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setRead(false);

        return this.notificationRepository.save(notification);
    }

    @Transactional
    public Optional<Notification> markAsRead(Long notificationId, User user) {
        if (user == null) return Optional.empty();
        Optional<Notification> notificationOpt = notificationRepository.findByIdAndUser(notificationId, user);
        notificationOpt.ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
        return notificationOpt;
    }

    @Transactional
    public void markAllAsReadForUser(User user) {
        if (user == null) return;
        notificationRepository.markAllAsReadForUser(user);
    }

    @Transactional
    public boolean deleteNotification(Long notificationId, User user) {
        if (user == null) return false;
        Optional<Notification> notificationOpt = notificationRepository.findByIdAndUser(notificationId, user);
        if (notificationOpt.isPresent()) {
            notificationRepository.delete(notificationOpt.get());
            return true;
        }
        return false;
    }
    @Transactional
    public void deleteAllNotificationsForUser(User user) {
        if (user == null) {
            return;
        }
        notificationRepository.deleteByUser(user);
    }

    public List<Notification> getUnreadNotifications(User user) {
        if (user == null) return List.of();
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(user);
    }
}
