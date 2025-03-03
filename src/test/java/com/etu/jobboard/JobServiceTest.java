package com.etu.jobboard;

import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.repository.JobRepository;
import com.etu.jobboard.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private User employer;
    private Job job1, job2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test user
        employer = new User();
        employer.setId(1L);
        employer.setName("Test Employer");
        employer.setEmail("test@example.com");
        employer.setRole(User.Role.EMPLOYER);

        // Create test jobs
        job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Test Job 1");
        job1.setDescription("This is a test job description");
        job1.setEmployer(employer);
        job1.setStatus(Job.Status.ACTIVE);
        job1.setPostDate(LocalDate.now());

        job2 = new Job();
        job2.setId(2L);
        job2.setTitle("Test Job 2");
        job2.setDescription("This is another test job description");
        job2.setEmployer(employer);
        job2.setStatus(Job.Status.CLOSED);
        job2.setPostDate(LocalDate.now().minusDays(10));
    }

    @Test
    void findAllJobs() {
        // Prepare test data
        List<Job> jobs = new ArrayList<>();
        jobs.add(job1);
        jobs.add(job2);

        // Set up Mock behavior
        when(jobRepository.findAll()).thenReturn(jobs);

        // Execute test
        List<Job> result = jobService.findAllJobs();

        // Verify results
        assertEquals(2, result.size());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void findActiveJobs() {
        // Prepare test data
        List<Job> activeJobs = new ArrayList<>();
        activeJobs.add(job1);

        // Set up Mock behavior
        when(jobRepository.findByStatus(Job.Status.ACTIVE)).thenReturn(activeJobs);

        // Execute test
        List<Job> result = jobService.findActiveJobs();

        // Verify results
        assertEquals(1, result.size());
        assertEquals("Test Job 1", result.get(0).getTitle());
        verify(jobRepository, times(1)).findByStatus(Job.Status.ACTIVE);
    }

    @Test
    void createJob() {
        // Prepare test data
        Job newJob = new Job();
        newJob.setTitle("New Job");
        newJob.setDescription("New job description");
        newJob.setEmployer(employer);

        // Set up Mock behavior
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> {
            Job savedJob = invocation.getArgument(0);
            savedJob.setId(3L);
            return savedJob;
        });

        // Execute test
        Job result = jobService.createJob(newJob);

        // Verify results
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Job", result.getTitle());
        assertEquals(Job.Status.ACTIVE, result.getStatus());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void closeJob() {
        // Set up Mock behavior
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));
        when(jobRepository.save(any(Job.class))).thenReturn(job1);

        // Execute test
        Job result = jobService.closeJob(1L);

        // Verify results
        assertEquals(Job.Status.CLOSED, result.getStatus());
        verify(jobRepository, times(1)).findById(1L);
        verify(jobRepository, times(1)).save(job1);
    }

    @Test
    void isJobOwner() {
        // Set up Mock behavior
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        // Execute test - same employer
        boolean result1 = jobService.isJobOwner(1L, employer);

        // Create different employer
        User otherEmployer = new User();
        otherEmployer.setId(2L);
        boolean result2 = jobService.isJobOwner(1L, otherEmployer);

        // Verify results
        assertTrue(result1);
        assertFalse(result2);
    }
}
