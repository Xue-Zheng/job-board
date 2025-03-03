package com.etu.jobboard.controller;

import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.findActiveJobs());
        return "jobs/list";
    }

    @GetMapping("/{id}")
    public String viewJob(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        model.addAttribute("job", job);
        return "jobs/view";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String showCreateJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "jobs/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String createJob(@Valid @ModelAttribute Job job,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal User currentUser,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jobs/create";
        }

        try {
            job.setEmployer(currentUser);
            jobService.createJob(job);
            redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
            return "redirect:/jobs";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Post created fail! " + e.getMessage());
            return "redirect:/jobs/create";
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String showEditJobForm(@PathVariable Long id,
                                  Model model,
                                  @AuthenticationPrincipal User currentUser,
                                  RedirectAttributes redirectAttributes) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        if (!jobService.isJobOwner(id, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this post");
            return "redirect:/jobs";
        }

        model.addAttribute("job", job);
        return "jobs/edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String updateJob(@PathVariable Long id,
                            @Valid @ModelAttribute Job job,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal User currentUser,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jobs/edit";
        }

        if (!jobService.isJobOwner(id, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this post");
            return "redirect:/jobs";
        }

        try {
            Job existingJob = jobService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
            job.setEmployer(existingJob.getEmployer());
            job.setId(id);

            jobService.updateJob(job);
            redirectAttributes.addFlashAttribute("successMessage", "Position updated successfully!");
            return "redirect:/jobs/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update position: " + e.getMessage());
            return "redirect:/jobs/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String deleteJob(@PathVariable Long id,
                            @AuthenticationPrincipal User currentUser,
                            RedirectAttributes redirectAttributes) {
        // 检查是否是职位所有者或管理员
        if (!jobService.isJobOwner(id, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this post");
            return "redirect:/jobs";
        }

        try {
            jobService.deleteJob(id);
            redirectAttributes.addFlashAttribute("successMessage", "Post deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Post deletion failed. " + e.getMessage());
        }
        return "redirect:/jobs";
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String closeJob(@PathVariable Long id,
                           @AuthenticationPrincipal User currentUser,
                           RedirectAttributes redirectAttributes) {
        // 检查是否是职位所有者或管理员
        if (!jobService.isJobOwner(id, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have the right to close this post");
            return "redirect:/jobs";
        }

        try {
            jobService.closeJob(id);
            redirectAttributes.addFlashAttribute("successMessage", "Position closed!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Post Closed Failed. " + e.getMessage());
        }
        return "redirect:/jobs/" + id;
    }

    @PostMapping("/{id}/reopen")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String reopenJob(@PathVariable Long id,
                            @AuthenticationPrincipal User currentUser,
                            RedirectAttributes redirectAttributes) {
        // 检查是否是职位所有者或管理员
        if (!jobService.isJobOwner(id, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "ou do not have the right to close this post");
            return "redirect:/jobs";
        }

        try {
            jobService.reopenJob(id);
            redirectAttributes.addFlashAttribute("successMessage", "Position has been reopened!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Job Reopening Failed. " + e.getMessage());
        }
        return "redirect:/jobs/" + id;
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String listMyJobs(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("jobs", jobService.findJobsByEmployer(currentUser));
        return "jobs/my-jobs";
    }
}
