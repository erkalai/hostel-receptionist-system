package com.hostelbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hostelbooking.service.ComplaintService;

@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {

	@Autowired
	private ComplaintService complaintService;

	@GetMapping("/dashboard")
	public String receptionistDashboard(Model model) {

		long pendingComplaintsCount = complaintService.countPendingComplaints();
		model.addAttribute("pendingComplaintsCount", pendingComplaintsCount);

		if (pendingComplaintsCount > 0) {
			model.addAttribute("warningMessage", "You have " + pendingComplaintsCount + " pending complaints.");
		}

//    	  List<Complaint> pendingComplaints = complaintService.getPendingComplaints();
//        model.addAttribute("pendingComplaints", pendingComplaints);
		return "receptionista/dashboard";
	}

}
