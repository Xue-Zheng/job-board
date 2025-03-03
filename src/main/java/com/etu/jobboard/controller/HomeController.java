package com.etu.jobboard.controller;

import com.etu.jobboard.model.User;
import com.etu.jobboard.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final JobService jobService;

    @Autowired
    public HomeController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Display active jobs on the homepage
        model.addAttribute("jobs", jobService.findActiveJobs());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "profile";
    }
}
