package com.hostelbooking.controller;


import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.PreBookedGuest;
import com.hostelbooking.entity.Program;
import com.hostelbooking.entity.Room;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.GuestStatus;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.PreBookedGuestRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.repository.UserRepository;
import com.hostelbooking.service.BookingService;
import com.hostelbooking.service.GuestService;
import com.hostelbooking.service.ProgramService;
import com.hostelbooking.service.RoomService;
import com.hostelbooking.service.UserService;


@Controller
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private GuestService guestService;

	  @Autowired
	    private BookingRepository bookingRepository;

	    @Autowired
	    private RoomRepository roomRepository;


	    @Autowired
	    private GuestRepository guestRepository;
	    
	    
	    @Autowired
	    private ProgramService programService; 

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private UserRepository userRepository;
	    
	    @Autowired
	    private PreBookedGuestRepository preBookedGuestRepository;
	    // Show Check In Form
	    @GetMapping("/checkin")
	    public String showCheckInPage(Model model, Principal principal) {
	    	
	    	User user = userService.findUserByEmail(principal.getName());

			model.addAttribute("userName", user);
	    	
	        List<Program> allPrograms = programService.findAllPrograms();

	        List<Integer> floors = roomRepository.findDistinctFloors();
	        model.addAttribute("floors", floors);
	        
	        List<Program> starredPrograms = allPrograms.stream()
	                .filter(Program::getIsStarred)
	                .collect(Collectors.toList());

	        List<Program> otherPrograms = allPrograms.stream()
	                .filter(program -> !program.getIsStarred())
	                .collect(Collectors.toList());

	        
	        model.addAttribute("starredPrograms", starredPrograms);
	        model.addAttribute("otherPrograms", otherPrograms);
	    	
	    	
	        model.addAttribute("guest", new Guest());
	        model.addAttribute("availableRooms", roomService.getAvailableRooms());
	        return "bookings/checkin";
	    }
	    
	    @PostMapping("/checkin")
	    public String checkInGuest(@RequestParam(required = false) String kid, @RequestParam Long roomId,
	                               @RequestParam String name, @RequestParam String idType, @RequestParam String idNumber,
	                               @RequestParam String mobileNumber, @RequestParam String programName,
	                               @RequestParam String foodType, @RequestParam String coffeeTime,
	                               @RequestParam String gender,
	                               RedirectAttributes redirectAttributes, Principal principal) {

	        try {
	        	
	            User user = userRepository.findByEmail(principal.getName());
	            Long userId = user.getId();

	            
	            System.out.println("********************* Inside Try" + userId);
	            
	            
	            
	            Optional<PreBookedGuest> optionalPreBookedGuest = preBookedGuestRepository.findByKid(kid);

	            System.out.println("********************* Inside Try" + optionalPreBookedGuest);
	            if (optionalPreBookedGuest.isPresent()) {
	                PreBookedGuest preBookedGuest = optionalPreBookedGuest.get();
	                
	                Guest guest = new Guest();
	                guest.setProgramName(preBookedGuest.getProgramName());
	                guest.setName(preBookedGuest.getName());
	                guest.setIdType(preBookedGuest.getIdType());
	                guest.setIdNumber(preBookedGuest.getIdNumber());
	                guest.setMobileNumber(preBookedGuest.getMobileNumber());
	                guest.setKid(preBookedGuest.getKid());
	            	guest.setFoodType(foodType);
	    			guest.setCoffeeTime(coffeeTime);
	    			guest.setGender(gender);
	                bookingService.checkIn(guest, roomId, userId);

	                System.out.println("********************* Pre Booked Go to Booking Service After");
	                
	                preBookedGuestRepository.delete(preBookedGuest);

	                redirectAttributes.addFlashAttribute("message", "Pre-booked guest successfully checked in.");
	                
	                
	            } else {
	    			Guest guest = new Guest();
	    			guest.setName(name);
	    			guest.setIdType(idType);
	    			guest.setIdNumber(idNumber);
	    			guest.setProgramName(programName);
	    			guest.setMobileNumber(mobileNumber);
	    			guest.setMobileNumber(mobileNumber);
	    			guest.setKid(kid);
	    			guest.setFoodType(foodType);
	    			guest.setCoffeeTime(coffeeTime);

	                bookingService.checkIn(guest, roomId, userId);

	                System.out.println("********************* New Booking Go to Booking Service After");
	                
	                redirectAttributes.addFlashAttribute("message", "Normal guest successfully checked in.");
	                return "redirect:/bookings/checkin";
	            }
	            
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("error", "Error during check-in: " + e.getMessage());
	        }

	        return "redirect:/bookings/checkin";
	    }



	    
	    @GetMapping("/checkout")
	    public String showCheckoutForm(Model model, Principal principal) {
	    	User user = userService.findUserByEmail(principal.getName());

			model.addAttribute("userName", user);
	    	
	    	
	        List<Integer> floors = roomRepository.findDistinctFloors();
	        
	        List<Room> rooms = roomRepository.findAll();

	        List<Room> occupiedRooms = roomService.getOccupiedRooms();
	        model.addAttribute("floors", floors);
	        model.addAttribute("rooms", rooms);
	        
	        
	        List<Room> bookedRooms = roomService.getAllRooms();
	        List<Guest> guests = guestRepository.findAll();
	        
	        model.addAttribute("occupiedRooms", occupiedRooms);
//	        model.addAttribute("rooms", bookedRooms);
	        model.addAttribute("guests", guests);
	        model.addAttribute("floors", floors);
	        
	        System.out.println(floors);
	        return "bookings/checkout";
	    }

	    @PostMapping("/checkout")
		public RedirectView checkOut(@RequestParam Long guestId, RedirectAttributes attributes, Principal principal) {
			try {

				User user = userRepository.findByEmail(principal.getName());

				Long userId = user.getId();

				Booking booking = bookingService.checkOut(guestId, userId);

				attributes.addFlashAttribute("booking", booking);

				return new RedirectView("/bookings/checkout");
			} catch (RuntimeException e) {
				
				attributes.addFlashAttribute("error", e.getMessage());
				return new RedirectView("/error");
			}
		}
	    
	    
	    
	    @GetMapping("/guests/by-room")
	    @ResponseBody
	    public List<Guest> getGuestsByRoom(@RequestParam Long roomId) {
	        System.out.println("Fetching guests for Room ID: " + roomId);
	        List<Guest> guests = bookingRepository.findGuestsByRoomIdAndStatus(roomId);
	        System.out.println("Guests: " + guests);
	        return guests;
	    }
	    
	     

	    @GetMapping("/get-booking-id-by-guest")
	    @ResponseBody
	    public Optional<Booking> getBookingIdByGuest(@RequestParam("guestId") Long guestId) {
	        // Fetch the booking ID using the guest ID
	        Optional<Booking> booking = bookingRepository.findBookingByGuestId(guestId);
	        
	        System.out.println("Booking Details : " + booking.get());
	        return booking;
	    }
	    
	    
	    
	    
	    //--------------------------------Chart
	    
	    
	    @GetMapping("/daily")
	    public ResponseEntity<Map<String, Long>> getDailyCounts() {
	        Map<String, Long> counts = bookingService.getDailyCounts();
	        System.out.println("********************"+ counts);
	        return ResponseEntity.ok(counts);
	    }

	    @GetMapping("/monthly")
	    public ResponseEntity<List<Map<String, Object>>> getMonthlyCounts() {
	        List<Map<String, Object>> counts = bookingService.getMonthlyCounts();
	        System.out.println("********************"+ counts);
	        return ResponseEntity.ok(counts);
	    }

	    @GetMapping("/yearly")
	    public ResponseEntity<List<Map<String, Object>>> getYearlyCounts() {
	        List<Map<String, Object>> counts = bookingService.getYearlyCounts();
	        System.out.println("********************"+ counts);
	        return ResponseEntity.ok(counts);
	    }
	    //-----------------------------------
	    
	
	    
	    
	    //_----------------Report
	    
	    @GetMapping("/report")
	    public String bookingManagement(
	            @RequestParam(value = "status", required = false) String status,
	            @RequestParam(value = "startDate", required = false) String startDate,
	            @RequestParam(value = "endDate", required = false) String endDate,
	            Model model, Principal principal) {

	        User user = userService.findUserByEmail(principal.getName());
	        model.addAttribute("userName", user);

	        // Fetch bookings based on the filter criteria
	        List<Booking> bookings = bookingService.findBookingsWithFilters(status, startDate, endDate);
	        model.addAttribute("bookings", bookings);
	        model.addAttribute("status", status);
	        model.addAttribute("startDate", startDate);
	        model.addAttribute("endDate", endDate);

	        return "common/manageBookings";
	    }


	    
	    //-------------------Pre Entry Guests
	    
	    
	    @GetMapping("/preEntry")
	    public String preEntryCheckInPage(Model model, Principal principal) {
	    	
	    	User user = userService.findUserByEmail(principal.getName());

			model.addAttribute("userName", user);
	    	
	        List<Program> allPrograms = programService.findAllPrograms();

	        List<Integer> floors = roomRepository.findDistinctFloors();
	        model.addAttribute("floors", floors);
	        
	        List<Program> starredPrograms = allPrograms.stream()
	                .filter(Program::getIsStarred)
	                .collect(Collectors.toList());

	        List<Program> otherPrograms = allPrograms.stream()
	                .filter(program -> !program.getIsStarred())
	                .collect(Collectors.toList());

	        model.addAttribute("starredPrograms", starredPrograms);
	        model.addAttribute("otherPrograms", otherPrograms);
	    	
	    	
	        model.addAttribute("guest", new Guest());
	        model.addAttribute("availableRooms", roomService.getAvailableRooms());
	        return "bookings/preEntry";
	    }


}
