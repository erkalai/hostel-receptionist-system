package com.hostelbooking.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hostelbooking.entity.PreBookedGuest;
import com.hostelbooking.repository.PreBookedGuestRepository;
import com.hostelbooking.service.GuestService;

@Controller
@RequestMapping("/guests")
public class GuestController {

	private final GuestService guestService;

	private PreBookedGuestRepository preBookedGuestRepository;

	@Autowired
	public GuestController(GuestService guestService, PreBookedGuestRepository preBookedGuestRepository) {
		this.guestService = guestService;
		this.preBookedGuestRepository = preBookedGuestRepository;
	}

	@GetMapping("/fetch")
	@ResponseBody
	public ResponseEntity<?> fetchGuestDetails(@RequestParam("kid") String kid) {
	    PreBookedGuest guest = preBookedGuestRepository.findByKid(kid)
	        .orElse(null);

	    if (guest == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guest not found");
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("name", guest.getName());
	    response.put("programName", guest.getProgramName());
	    response.put("idType", guest.getIdType());
	    response.put("idNumber", guest.getIdNumber());
	    response.put("mobileNumber", guest.getMobileNumber());
System.out.println("*********************" + response);
	    return ResponseEntity.ok(response);
	}


}