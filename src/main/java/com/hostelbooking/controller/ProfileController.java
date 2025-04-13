package com.hostelbooking.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.entity.User;
import com.hostelbooking.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String showProfilePage(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		return "common/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, Principal principal, RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		User user = userService.findByUsername(username);

		// Check if the old password matches
		if (!userService.checkPassword(user, oldPassword)) {
			redirectAttributes.addFlashAttribute("error", "Old password is incorrect.");
			return "redirect:/profile";
		}

		// Check if the new password and confirm password match
		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
			return "redirect:/profile";
		}

		// Update the password
		userService.updatePassword(user, newPassword);

		redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
		return "redirect:/profile";
	}
}
