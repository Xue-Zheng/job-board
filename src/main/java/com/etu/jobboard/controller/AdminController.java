package com.etu.jobboard.controller;

import com.etu.jobboard.model.User;
import com.etu.jobboard.service.ApplicationService;
import com.etu.jobboard.service.JobService;
import com.etu.jobboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @Autowired
    public AdminController(UserService userService, JobService jobService, ApplicationService applicationService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userService.findAllUsers().size());
        model.addAttribute("jobCount", jobService.findAllJobs().size());
        model.addAttribute("applicationCount", applicationService.findAllApplications().size());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("jobs", jobService.findAllJobs());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User has been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/jobs")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.findAllJobs());
        return "admin/jobs";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobService.deleteJob(id);
            redirectAttributes.addFlashAttribute("successMessage", "Job has been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete job: " + e.getMessage());
        }
        return "redirect:/admin/jobs";
    }

    @GetMapping("/applications")
    public String listApplications(Model model) {
        model.addAttribute("applications", applicationService.findAllApplications());
        return "admin/applications";
    }
}
