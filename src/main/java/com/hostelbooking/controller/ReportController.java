package com.hostelbooking.controller;

import com.hostelbooking.dto.BookingReportDTO;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.UserRepository;
import com.hostelbooking.service.BookingService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {
	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String getFilteredBookings(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year, Model model,
			Principal principal) {

		// Fetch the logged-in user's email
		String email = principal.getName();

		// Find the user by email (using your method)
		User user = userRepository.findByEmail(email);

		// Add user and reports data to the model
		model.addAttribute("user", user);

		List<BookingReportDTO> reports = bookingService.getFilteredBookings(startDate, endDate, month, year);
		model.addAttribute("reports", reports);
		return "bookings/report";
	}
    
   
}
