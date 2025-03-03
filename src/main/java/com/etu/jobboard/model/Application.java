package com.etu.jobboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_id", "applicant_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate applicationDate;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    // 申请状态枚举
    public enum Status {
        PENDING,    // 待处理
        ACCEPTED,   // 已接受
        REJECTED    // 已拒绝
    }

    // 在申请前自动设置申请日期
    @PrePersist
    public void prePersist() {
        if (applicationDate == null) {
            applicationDate = LocalDate.now();
        }
        if (status == null) {
            status = Status.PENDING;
        }
    }
}
