package com.Jobs.JobsMemory.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReminderCreateDTO {

    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private Long jobApplicationId;
}
