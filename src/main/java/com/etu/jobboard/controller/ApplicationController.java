package com.etu.jobboard.controller;

import com.etu.jobboard.model.Application;
import com.etu.jobboard.model.Job;
import com.etu.jobboard.model.User;
import com.etu.jobboard.service.ApplicationService;
import com.etu.jobboard.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, JobService jobService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public String listMyApplications(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("applications", applicationService.findApplicationsByApplicant(currentUser));
        return "applications/my-applications";
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String listJobApplications(@PathVariable Long jobId,
                                      @AuthenticationPrincipal User currentUser,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        Job job = jobService.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));

        // Check if the user is the job owner or an admin
        if (!jobService.isJobOwner(jobId, currentUser) && !currentUser.getRole().equals(User.Role.ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to view applications for this job.");
            return "redirect:/jobs";
        }

        model.addAttribute("job", job);
        model.addAttribute("applications", applicationService.findApplicationsByJob(job));
        return "applications/applicants";
    }

    @PostMapping("/job/{jobId}/apply")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public String applyForJob(@PathVariable Long jobId,
                              @RequestParam String coverLetter,
                              @AuthenticationPrincipal User currentUser,
                              RedirectAttributes redirectAttributes) {
        try {
            applicationService.applyForJob(jobId, currentUser, coverLetter);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit application: " + e.getMessage());
        }
        return "redirect:/jobs/" + jobId;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN', 'JOB_SEEKER')")
    public String viewApplication(@PathVariable Long id,
                                  @AuthenticationPrincipal User currentUser,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Application application = applicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + id));

        // Ensure only the applicant, employer, or admin can view
        boolean isApplicant = application.getApplicant().getId().equals(currentUser.getId());
        boolean isEmployer = application.getJob().getEmployer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals(User.Role.ADMIN);

        if (!isApplicant && !isEmployer && !isAdmin) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to view this application.");
            return "redirect:/jobs";
        }

        model.addAttribute("application", application);
        return "applications/view";
    }

    @PostMapping("/{id}/update-status")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String updateApplicationStatus(@PathVariable Long id,
                                          @RequestParam Application.Status status,
                                          @AuthenticationPrincipal User currentUser,
                                          RedirectAttributes redirectAttributes) {
        Application application = applicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + id));

        // Check if the user is the job owner or an admin
        boolean isEmployer = application.getJob().getEmployer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals(User.Role.ADMIN);

        if (!isEmployer && !isAdmin) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to update this application status.");
            return "redirect:/jobs";
        }

        try {
            applicationService.updateApplicationStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Application status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update status: " + e.getMessage());
        }

        return "redirect:/applications/job/" + application.getJob().getId();
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public String withdrawApplication(@PathVariable Long id,
                                      @AuthenticationPrincipal User currentUser,
                                      RedirectAttributes redirectAttributes) {
        Application application = applicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + id));

        // Check if the user is the applicant
        if (!application.getApplicant().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to withdraw this application.");
            return "redirect:/applications/my-applications";
        }

        try {
            applicationService.deleteApplication(id);
            redirectAttributes.addFlashAttribute("successMessage", "Application withdrawn successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to withdraw application: " + e.getMessage());
        }

        return "redirect:/applications/my-applications";
    }
}
