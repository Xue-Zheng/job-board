package com.etu.jobboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title cannot be empty")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Job desc cannot be empty")
    private String description;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate postDate;

    private LocalDate deadlineDate;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    // 职位状态枚举
    public enum Status {
        ACTIVE,     // 活跃
        CLOSED      // 已关闭
    }

    // 在发布前自动设置发布日期
    @PrePersist
    public void prePersist() {
        if (postDate == null) {
            postDate = LocalDate.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }
}
