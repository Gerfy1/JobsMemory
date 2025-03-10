package com.Jobs.JobsMemory.service;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.repository.JobApplicationRepository;
import com.Jobs.JobsMemory.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private UserRepository userRepository;

    public JobApplicationService() {
    }

    public List<JobApplication> getAllJobApplications() {
        return this.jobApplicationRepository.findAll();
    }

    public JobApplication saveJobApplication(JobApplication jobApplication) {
        return (JobApplication)this.jobApplicationRepository.save(jobApplication);
    }

    public JobApplication updateJobApplication(Long id, String status) {
        JobApplication jobApplication = (JobApplication)this.jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Application not found"));
        jobApplication.setStatus(status);
        return (JobApplication)this.jobApplicationRepository.save(jobApplication);
    }

    public void deleteJobApplication(Long id) {
        this.jobApplicationRepository.deleteById(id);
    }

    public Optional<JobApplication> getJobApplicationById(Long id) {
        return this.jobApplicationRepository.findById(id);
    }

    public List<JobApplication> findByUser(User user) {
        return this.jobApplicationRepository.findByUser(user);
    }
}
