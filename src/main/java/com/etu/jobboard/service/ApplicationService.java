package com.etu.jobboard.service;

import com.etu.jobboard.model.Application;
import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.repository.ApplicationRepository;
import com.etu.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Application> findApplicationsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    @Transactional(readOnly = true)
    public List<Application> findApplicationsByApplicant(User applicant) {
        return applicationRepository.findByApplicant(applicant);
    }

    @Transactional(readOnly = true)
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Application> findByJobAndApplicant(Job job, User applicant) {
        return applicationRepository.findByJobAndApplicant(job, applicant);
    }

    @Transactional
    public Application applyForJob(Long jobId, User applicant, String coverLetter) {
        // 检查职位是否存在
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        // 检查职位是否活跃
        if (job.getStatus() != Job.Status.ACTIVE) {
            throw new IllegalStateException("Cannot apply for a closed job");
        }

        // 检查是否已经申请过
        if (applicationRepository.existsByJobAndApplicant(job, applicant)) {
            throw new IllegalStateException("You have already applied for this job");
        }

        // 创建并保存申请
        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setStatus(Application.Status.PENDING);
        application.setCoverLetter(coverLetter);

        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateApplicationStatus(Long id, Application.Status status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean hasAppliedForJob(Job job, User applicant) {
        return applicationRepository.existsByJobAndApplicant(job, applicant);
    }
}
