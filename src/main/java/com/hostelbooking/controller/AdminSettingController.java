package com.hostelbooking.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.entity.User;
import com.hostelbooking.service.SystemSettingService;
import com.hostelbooking.service.UserService;

@Controller
@RequestMapping("/settings")
public class AdminSettingController {

	@Autowired
	private SystemSettingService systemSettingService;

	@Autowired
	private UserService userService;

	// --------- Show Settings Menu
	@GetMapping
	public String showSettings(Model model, Principal principal) {
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		boolean allowRegistration = systemSettingService.isRegistrationAllowed();
		model.addAttribute("allowRegistration", allowRegistration);
		return "admin/settings";
	}

	// --------- Update Settings
	@PostMapping("/update-registration")
	public String updateRegistrationStatus(
			@RequestParam(value = "allowRegistration", defaultValue = "false") boolean allowRegistration) {
		systemSettingService.updateRegistrationStatus(allowRegistration);
		return "redirect:/settings";
	}

}
