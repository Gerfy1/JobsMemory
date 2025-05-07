package com.Jobs.JobsMemory.repository;

import com.Jobs.JobsMemory.model.Notification;
import java.util.List;
import java.util.Optional;

import com.Jobs.JobsMemory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Optional<Notification> findByIdAndUser(Long id, User user);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user AND n.read = false")
    int markAllAsReadForUser(User user);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);

}