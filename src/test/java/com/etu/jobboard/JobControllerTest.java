package com.etu.jobboard;

import com.etu.jobboard.controller.JobController;
import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.service.JobService;
import com.etu.jobboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private UserService userService;

    private Job testJob;
    private User testEmployer;

    @BeforeEach
    void setUp() {
        // Set up test user
        testEmployer = new User();
        testEmployer.setId(1L);
        testEmployer.setName("Test Employer");
        testEmployer.setEmail("employer@test.com");
        testEmployer.setRole(User.Role.EMPLOYER);

        // Set up test job
        testJob = new Job();
        testJob.setId(1L);
        testJob.setTitle("Test Job");
        testJob.setDescription("This is a test job description");
        testJob.setEmployer(testEmployer);
        testJob.setStatus(Job.Status.ACTIVE);
        testJob.setPostDate(LocalDate.now());
    }

    @Test
    @WithMockUser(username = "user", roles = "JOB_SEEKER")
    void testListJobs() throws Exception {
        when(jobService.findActiveJobs()).thenReturn(Arrays.asList(testJob));

        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/list"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    @WithMockUser(username = "user", roles = "JOB_SEEKER")
    void testViewJob() throws Exception {
        when(jobService.findById(anyLong())).thenReturn(Optional.of(testJob));

        mockMvc.perform(get("/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/view"))
                .andExpect(model().attributeExists("job"));
    }

    @Test
    @WithMockUser(username = "employer@test.com", roles = "EMPLOYER")
    void testShowCreateJobForm() throws Exception {
        mockMvc.perform(get("/jobs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/create"))
                .andExpect(model().attributeExists("job"));
    }

    @Test
    @WithMockUser(username = "employer@test.com", roles = "EMPLOYER")
    void testCreateJob() throws Exception {
        when(jobService.createJob(any(Job.class))).thenReturn(testJob);

        mockMvc.perform(post("/jobs/create")
                        .with(csrf())
                        .param("title", "New Job")
                        .param("description", "New job description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jobs"));
    }

    @Test
    @WithMockUser(username = "employer@test.com", roles = "EMPLOYER")
    void testShowEditJobForm() throws Exception {
        when(jobService.findById(anyLong())).thenReturn(Optional.of(testJob));
        when(jobService.isJobOwner(anyLong(), any(User.class))).thenReturn(true);

        mockMvc.perform(get("/jobs/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs/edit"))
                .andExpect(model().attributeExists("job"));
    }
}
