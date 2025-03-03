package com.etu.jobboard.dto;

import com.etu.jobboard.model.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private Long id;

    @NotBlank(message = "Job title cannot be empty")
    private String title;

    @NotBlank(message = "Job desc cannot be empty")
    private String description;

    private Long employerId;

    private Job.Status status;

    private LocalDate postDate;

    private LocalDate deadlineDate;

    // 从Job实体转换为DTO
    public static JobDto fromEntity(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .employerId(job.getEmployer() != null ? job.getEmployer().getId() : null)
                .status(job.getStatus())
                .postDate(job.getPostDate())
                .deadlineDate(job.getDeadlineDate())
                .build();
    }

    // 转换为Job实体
    public Job toEntity() {
        Job job = new Job();
        job.setId(this.id);
        job.setTitle(this.title);
        job.setDescription(this.description);
        job.setStatus(this.status != null ? this.status : Job.Status.ACTIVE);
        job.setPostDate(this.postDate != null ? this.postDate : LocalDate.now());
        job.setDeadlineDate(this.deadlineDate);
        return job;
    }
}
