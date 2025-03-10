package com.Jobs.JobsMemory.repository;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
}