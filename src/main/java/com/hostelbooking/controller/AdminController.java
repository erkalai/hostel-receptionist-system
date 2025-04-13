package com.hostelbooking.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.dto.UserDto;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.RoleType;
import com.hostelbooking.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@GetMapping("/createUser")
	public String showCreateUserPage(Model model, Principal principal) {
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		model.addAttribute("user", new UserDto());
		model.addAttribute("roles", Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList()));
		return "admin/create-user";
	}

	@PostMapping("/createUser")
	public String createUser(@ModelAttribute UserDto userDto, Model model) {
		try {
			userService.saveUser(userDto);
			return "redirect:/admin/users?success";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", "Creation failed: " + e.getMessage());
			return "redirect:/admin/createUser";
		}
	}

	@GetMapping("/editUser/{id}")
	public String showEditUserPage(@PathVariable("id") Long id, Model model) {
		User user = userService.findUserById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

		UserDto userDto = new UserDto();
		String[] nameParts = user.getName().split(" ", 2);
		userDto.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
		userDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
		userDto.setEmail(user.getEmail());
		userDto.setRoleName(user.getRoles().stream().findFirst().map(role -> role.getName().name()).orElse(""));

		// Set user id for the form
		userDto.setId(id);

		model.addAttribute("userDto", userDto);
		model.addAttribute("roles", Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList()));
		return "admin/editUser";
	}

	// -----------------------

	@PostMapping("/updateUser/{id}")
	public String updateUser(@PathVariable Long id, @ModelAttribute("userDto") UserDto updatedUser, Model model,
			RedirectAttributes redirectAttributes) throws Throwable {

		// Log received data for debugging
		System.out.println("Password received: " + updatedUser.getPassword());
		System.out.println("Updated user data: " + updatedUser);

		try {
			if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
				String encodedPassword = new BCryptPasswordEncoder().encode(updatedUser.getPassword());
				updatedUser.setPassword(encodedPassword);
				System.out.println("Encoded password: " + encodedPassword);
			}

			userService.updateUserWithRoles(id, updatedUser);

			redirectAttributes.addFlashAttribute("success", "User updated successfully!");

		} catch (Exception e) {
			System.err.println("Error updating user: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
		}

		return "redirect:/admin/users";
	}

	// ------------Activate User
	@PostMapping("/activateUser/{id}")
	public String activateUser(@PathVariable("id") Long id) throws Throwable {
		userService.activateUser(id);
		return "redirect:/admin/users";
	}

	// ------------Deactivate User
	@PostMapping("/deactivateUser/{id}")
	public String deactivateUser(@PathVariable("id") Long id) throws Throwable {
		userService.deactivateUser(id);
		return "redirect:/admin/users";
	}


	// ---------Show Users and Activate, Deactivate, Edit Page
	@GetMapping("/users")
	public String viewUsers(Model model, Principal principal) {
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		model.addAttribute("user", new UserDto());
		model.addAttribute("roles", Arrays.stream(RoleType.values()).map(Enum::name).collect(Collectors.toList()));
		model.addAttribute("users", userService.getAllUsers());
		return "admin/users";
	}
}
