package com.etu.jobboard.dto;

import com.etu.jobboard.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private Long id;

    @NotNull(message = "Job ID cannot be null")
    private Long jobId;

    @NotNull(message = "Apply ID cannot be null")
    private Long applicantId;

    private Application.Status status;

    private LocalDate applicationDate;

    private String coverLetter;

    private String jobTitle;
    private String applicantName;

    public static ApplicationDto fromEntity(Application application) {
        return ApplicationDto.builder()
                .id(application.getId())
                .jobId(application.getJob() != null ? application.getJob().getId() : null)
                .applicantId(application.getApplicant() != null ? application.getApplicant().getId() : null)
                .status(application.getStatus())
                .applicationDate(application.getApplicationDate())
                .coverLetter(application.getCoverLetter())
                .jobTitle(application.getJob() != null ? application.getJob().getTitle() : null)
                .applicantName(application.getApplicant() != null ? application.getApplicant().getName() : null)
                .build();
    }

    public Application toEntity() {
        Application application = new Application();
        application.setId(this.id);
        application.setStatus(this.status != null ? this.status : Application.Status.PENDING);
        application.setApplicationDate(this.applicationDate != null ? this.applicationDate : LocalDate.now());
        application.setCoverLetter(this.coverLetter);
        return application;
    }
}
