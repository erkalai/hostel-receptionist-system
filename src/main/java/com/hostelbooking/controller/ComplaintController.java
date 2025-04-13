package com.hostelbooking.controller;



import java.awt.desktop.UserSessionEvent;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.entity.Complaint;
import com.hostelbooking.entity.User;
import com.hostelbooking.service.ComplaintService;
import com.hostelbooking.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/complaint")
public class ComplaintController {

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private UserService userService;
	
	 @Autowired
	    public ComplaintController(ComplaintService complaintService, UserService userService) {
	        this.complaintService = complaintService;
	        this.userService = userService;
	    }
	
    @GetMapping
    public String showComplaintForm(Model model, Principal principal) {
    	User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
    	
        model.addAttribute("complaint", new Complaint());
        return "common/complaint";
    }

    @PostMapping
    public String handleComplaintSubmit(@Valid @ModelAttribute Complaint complaint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Validation error: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/dashboard";
        }

        try {
            if (complaint.getStatus() == null) {
                complaint.setStatus(Complaint.Status.PENDING);
            }

            complaintService.saveComplaint(complaint);
            model.addAttribute("successMessage", "Complaint submitted successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error submitting complaint: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/update/{id}")
    public String updateComplaintStatus(@PathVariable Long id, @RequestParam String status, Model model) {
        try {
            Complaint complaint = complaintService.getComplaintById(id);
            complaint.setStatus(Complaint.Status.valueOf(status));
            if (Complaint.Status.COMPLETED == complaint.getStatus()) {
                complaint.setCompletedDate(LocalDate.now());
            }
            complaintService.saveComplaint(complaint);
            model.addAttribute("successMessage", "Complaint updated successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating complaint: " + e.getMessage());
        }
        return "redirect:/complaint/management";
    }

    @GetMapping("/management")
    public String complaintManagement(
    		@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
    		Model model, Principal principal) {
    	User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
    	

		// Fetch complaints based on the filter criteria
	    List<Complaint> complaints = complaintService.findComplaintsWithFilters(status, startDate, endDate);
	    model.addAttribute("complaints", complaints);
	    model.addAttribute("status", status);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
		
        return "common/manageComplaints";
    }
 

    
}
