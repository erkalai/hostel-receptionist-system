package com.hostelbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard"; // Redirect root URL to dashboard
    }
}
