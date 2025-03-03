package com.etu.jobboard.repository;

import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(Job.Status status);

    List<Job> findByEmployer(User employer);
}
