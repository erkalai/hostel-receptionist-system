package com.hostelbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    @GetMapping("/dashboard")
    public String moderatorDashboard() {
        return "moderator/dashboard";
    }

    
}
