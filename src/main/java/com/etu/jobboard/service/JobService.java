package com.etu.jobboard.service;

import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Job> findActiveJobs() {
        return jobRepository.findByStatus(Job.Status.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    @Transactional(readOnly = true)
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Transactional
    public Job createJob(Job job) {
        // 新职位默认为活跃状态
        job.setStatus(Job.Status.ACTIVE);
        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJob(Job job) {
        // 确保职位存在
        if (!jobRepository.existsById(job.getId())) {
            throw new IllegalArgumentException("Job not found");
        }
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Transactional
    public Job closeJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setStatus(Job.Status.CLOSED);
        return jobRepository.save(job);
    }

    @Transactional
    public Job reopenJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setStatus(Job.Status.ACTIVE);
        return jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public boolean isJobOwner(Long jobId, User user) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return false;
        }
        return jobOpt.get().getEmployer().getId().equals(user.getId());
    }
}
