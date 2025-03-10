package com.Jobs.JobsMemory.controller;

import com.Jobs.JobsMemory.model.JobApplication;

public class JobApplicationRequest {
    private JobApplication jobApplication;
    private Long userId;

    public JobApplicationRequest() {
    }

    public JobApplication getJobApplication() {
        return this.jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
