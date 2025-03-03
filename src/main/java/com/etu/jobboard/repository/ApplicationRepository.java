package com.etu.jobboard.repository;

import com.etu.jobboard.model.Application;
import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicant(User applicant);

    List<Application> findByJob(Job job);

    Optional<Application> findByJobAndApplicant(Job job, User applicant);

    boolean existsByJobAndApplicant(Job job, User applicant);
}
