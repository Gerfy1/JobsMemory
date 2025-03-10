package com.Jobs.JobsMemory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(
        name = "reminders"
)
public class Reminder {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    @Column(
            nullable = false
    )
    private String title;
    @Column(
            name = "description"
    )
    private String description;
    @Column(
            name = "reminder_date",
            nullable = false
    )
    private LocalDateTime date;
    @Column(
            name = "is_completed"
    )
    private boolean isCompleted;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "job_application_id"
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reminders"})
    private JobApplication jobApplication;
    @Transient
    private Long jobApplicationId;

    public Long getJobApplicationId() {
        return this.jobApplicationId;
    }

    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public JobApplication getJobApplication() {
        return this.jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Reminder)) {
            return false;
        } else {
            Reminder other = (Reminder)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getId() != other.getId()) {
                return false;
            } else if (this.isCompleted() != other.isCompleted()) {
                return false;
            } else {
                Object this$jobApplicationId = this.getJobApplicationId();
                Object other$jobApplicationId = other.getJobApplicationId();
                if (this$jobApplicationId == null) {
                    if (other$jobApplicationId != null) {
                        return false;
                    }
                } else if (!this$jobApplicationId.equals(other$jobApplicationId)) {
                    return false;
                }

                Object this$title = this.getTitle();
                Object other$title = other.getTitle();
                if (this$title == null) {
                    if (other$title != null) {
                        return false;
                    }
                } else if (!this$title.equals(other$title)) {
                    return false;
                }

                Object this$description = this.getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }

                Object this$date = this.getDate();
                Object other$date = other.getDate();
                if (this$date == null) {
                    if (other$date != null) {
                        return false;
                    }
                } else if (!this$date.equals(other$date)) {
                    return false;
                }

                Object this$jobApplication = this.getJobApplication();
                Object other$jobApplication = other.getJobApplication();
                if (this$jobApplication == null) {
                    if (other$jobApplication != null) {
                        return false;
                    }
                } else if (!this$jobApplication.equals(other$jobApplication)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Reminder;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        result = result * 59 + (this.isCompleted() ? 79 : 97);
        Object $jobApplicationId = this.getJobApplicationId();
        result = result * 59 + ($jobApplicationId == null ? 43 : $jobApplicationId.hashCode());
        Object $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        Object $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        Object $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : $date.hashCode());
        Object $jobApplication = this.getJobApplication();
        result = result * 59 + ($jobApplication == null ? 43 : $jobApplication.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        long var10000 = this.getId();
        return "Reminder(id=" + var10000 + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", date=" + this.getDate() + ", isCompleted=" + this.isCompleted() + ", jobApplication=" + this.getJobApplication() + ", jobApplicationId=" + this.getJobApplicationId() + ")";
    }

    @Generated
    public Reminder(final long id, final String title, final String description, final LocalDateTime date, final boolean isCompleted, final JobApplication jobApplication, final Long jobApplicationId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.jobApplication = jobApplication;
        this.jobApplicationId = jobApplicationId;
    }

    @Generated
    public Reminder() {
    }
}