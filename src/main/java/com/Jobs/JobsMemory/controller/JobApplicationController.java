package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.JobApplication;
import com.Jobs.JobsMemory.model.User;
import com.Jobs.JobsMemory.service.JobApplicationService;
import com.Jobs.JobsMemory.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/job-applications"})
@CrossOrigin(
        origins = {"https://login-angular-eight.vercel.app"}
)
public class JobApplicationController {
    @Autowired
    private JobApplicationService jobApplicationService;
    @Autowired
    private UserService userService;

    public JobApplicationController() {
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getJobApplications() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            User user = this.userService.findByUsername(userDetails.getUsername());
            List<JobApplication> jobApplications = this.jobApplicationService.findByUser(user);
            return ResponseEntity.ok(jobApplications);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        Optional<JobApplication> jobApplication = this.jobApplicationService.getJobApplicationById(id);
        return jobApplication.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public JobApplication createJobApplication(@RequestBody JobApplication jobApplication) {
        if (jobApplication.getUser() != null && jobApplication.getUser().getId() != null) {
            return this.jobApplicationService.saveJobApplication(jobApplication);
        } else {
            throw new IllegalArgumentException("User ID must not be null");
        }
    }

    @PutMapping({"/{id}/status"})
    public JobApplication updateJobApplicationStatus(@PathVariable Long id, @RequestParam String status) {
        return this.jobApplicationService.updateJobApplication(id, status);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id) {
        this.jobApplicationService.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }
}
