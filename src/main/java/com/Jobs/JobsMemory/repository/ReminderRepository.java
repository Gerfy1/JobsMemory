package com.Jobs.JobsMemory.repository;

import com.Jobs.JobsMemory.model.Reminder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByJobApplication_User_Id(Long userId);

    List<Reminder> findByJobApplication_Id(Long id);

    @Query("SELECT r from Reminder r where r.jobApplication.user.id = :userId AND r.date BETWEEN :startDate AND :endDate")
    List<Reminder> findUpcomingReminders(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Reminder r WHERE r.date BETWEEN :startDate AND :endDate")
    List<Reminder> findAllUpcomingReminders(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
