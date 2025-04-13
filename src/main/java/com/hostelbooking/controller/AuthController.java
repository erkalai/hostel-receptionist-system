package com.hostelbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.dto.UserDto;
import com.hostelbooking.entity.Role;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.RoleType;
import com.hostelbooking.repository.UserRepository;
import com.hostelbooking.service.AuthenticationService;
import com.hostelbooking.service.ComplaintService;
import com.hostelbooking.service.SystemSettingService;
import com.hostelbooking.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class AuthController {

	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final SystemSettingService systemSettingService;
	private ComplaintService complaintService;
	private UserRepository userRepository;

	@Autowired
	public AuthController(UserService userService, AuthenticationService authenticationService,
			SystemSettingService systemSettingService, ComplaintService complaintService,
			UserRepository userRepository) {
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.systemSettingService = systemSettingService;
		this.userRepository = userRepository;
		this.complaintService = complaintService;
	}

	@GetMapping("/register")
	public String showRegistrationPage(Model model) {
		model.addAttribute("user", new UserDto());
		model.addAttribute("roles", Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList()));
		return "register";
	}

	
	// Register Post Mapping
	@PostMapping("/register")
	public String registerUser(@ModelAttribute UserDto userDto, Model model) {
		try {
			
			if (!systemSettingService.isRegistrationAllowed()) {
				model.addAttribute("error", "Registration is currently disabled.");

				return "redirect:/error?error";
			}

			userService.saveUser(userDto);

			return "redirect:/register?success";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", "Registration failed: " + e.getMessage());

			return "redirect:/register?error";
		} catch (Exception e) {
			model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());

			return "redirect:/register";
		}
	}

	// Login page
	@GetMapping("/user/login")
	public String loginPage() {
		return "login";
	}


	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);

		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			return "redirect:/login?error";
		}

		// Get the first role of the user
		Role userRole = user.getRoles().stream().findFirst().orElse(null);

		if (userRole == null) {
			return "redirect:/login";
		}

		// Get the count of pending complaints
		long pendingComplaintsCount = complaintService.countPendingComplaints();
		model.addAttribute("pendingComplaintsCount", pendingComplaintsCount);

		model.addAttribute("user", new UserDto()); // For form binding
		model.addAttribute("roles", Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList()));
		model.addAttribute("users", userService.getAllUsers());

		// Match the role name to a RoleType and redirect
		switch (userRole.getName()) {
		case ADMIN:
			return "/admin/dashboard";
		case MODERATOR:
			return "/moderator/dashboard";
		case RECEPTIONIST:
			return "/receptionist/dashboard";
		default:
			return "redirect:/login";
		}
	}
}
