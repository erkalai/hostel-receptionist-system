package com.hostelbooking.controller;

import java.awt.desktop.UserSessionEvent;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.PreBookedGuest;
import com.hostelbooking.entity.Program;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.PreBookedGuestRepository;
import com.hostelbooking.repository.UserRepository;
import com.hostelbooking.service.ProgramService;
import com.hostelbooking.service.UserService;

@Controller
@RequestMapping("/preEntry")
public class PreBookedGuestController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProgramService programService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PreBookedGuestRepository preBookedGuestRepository;

	@GetMapping
	public String preEntryCheckInPage(Model model, Principal principal) {

		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);

		List<Program> allProgram = programService.findAllPrograms();

		List<Program> starredPrograms = allProgram.stream().filter(Program::getIsStarred).collect(Collectors.toList());

		List<Program> otherPrograms = allProgram.stream().filter(program -> !program.getIsStarred())
				.collect(Collectors.toList());

		model.addAttribute("otherPrograms", otherPrograms);
		model.addAttribute("starredPrograms", starredPrograms);

		model.addAttribute("guest", new Guest());

		return "bookings/preEntry";

	}

	@PostMapping
	public String savePreBookedGues(@RequestParam String name,
									@RequestParam String programName,
									@RequestParam String idType,
									@RequestParam String mobileNumber,
									@RequestParam String kid,
									@RequestParam String idNumber,
									RedirectAttributes redioAttributes,
									Principal principal) {

		try {
			User user = userRepository.findByEmail(principal.getName());
			Long userId = user.getId();

			PreBookedGuest preBookedGuest = new PreBookedGuest();
			preBookedGuest.setProgramName(programName);
			preBookedGuest.setName(name);
			preBookedGuest.setIdType(idType);
			preBookedGuest.setIdNumber(idNumber);
			preBookedGuest.setMobileNumber(mobileNumber);
			preBookedGuest.setStatus("NOT_PRESENT");
			preBookedGuest.setKid(kid);

			preBookedGuestRepository.save(preBookedGuest);

			return "redirect:/preEntry";

		} catch (Exception e) {

		}
		return "redirect:/preEntry";
	}

	@GetMapping("/findAllPreBookedGuest")
	public String getPreBookedGuests(Model model, Principal principal) {
		
		  User user = userService.findUserByEmail(principal.getName());
	        model.addAttribute("userName", user);
	    model.addAttribute("PreGuests", preBookedGuestRepository.findAll());
	    return "bookings/preBookedGuest";
	}

	@PostMapping("/guest/edit")
	public String editPreBookedGuest(@ModelAttribute PreBookedGuest guest) {
	    // Save updated guest details to database
	    preBookedGuestRepository.save(guest);
	    return "redirect:/findAllPreBookedGuest";  // Redirect back to the list after saving
	}

	@GetMapping("/guest/{kid}")
	@ResponseBody
	public PreBookedGuest getPreBookedGuestByKid(@PathVariable String kid) {
	    return preBookedGuestRepository.findByKidA(kid); // Assuming `findByKidA(kid)` returns a PreBookedGuest object
	}



}
