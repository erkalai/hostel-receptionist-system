package com.hostelbooking.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.Room;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.service.BookingService;
import com.hostelbooking.service.RoomService;
import com.hostelbooking.service.UserService;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/rooms")
public class RoomController {

	private RoomService roomService;

	private BookingService bookingService;

	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserService userService;

	@Autowired
	public RoomController(RoomService roomService, BookingService bookingService, RoomRepository roomRepository,
			GuestRepository guestRepository) {
		this.roomService = roomService;
		this.bookingService = bookingService;
		this.roomRepository = roomRepository;
		this.guestRepository = guestRepository;
	}

	@GetMapping("/available-rooms")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'RECEPTIONIST')")
	public String showAvailableRooms(Model model, Principal principal) {

		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);

		List<Room> availableRooms = roomService.getAvailableRooms();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", availableRooms);
		model.addAttribute("guests", guests);
		return "rooms/available-rooms";
	}

	@GetMapping("/available-rooms/bygender")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'RECEPTIONIST')")
	public String showAvailableRoomsMale(@RequestParam String gender, Model model, Principal principal) {

		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);

		String male = gender;
		System.out.println("************ " + male);

		List<Room> availableRoomsByGender = roomService.getAvailableRoomsByGender(male);
		System.out.println("************ " + availableRoomsByGender);
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", availableRoomsByGender);
		model.addAttribute("guests", guests);
		return "rooms/available-rooms";
	}

	@GetMapping("/all")
	public List<Map<String, Object>> getAllRooms() {
		List<Room> rooms = roomRepository.findAll();
		return rooms.stream().map(room -> {
			Map<String, Object> roomDetails = new HashMap<>();
			roomDetails.put("id", room.getId());
			roomDetails.put("roomNumber", room.getRoomNumber());
			roomDetails.put("floor", room.getFloor());
			return roomDetails;
		}).collect(Collectors.toList());
	}

	@GetMapping("/change-room")
	public String showChangeRoomPage(Model model, Principal principal) {
		List<Room> occupiedRooms = roomService.getOccupiedRooms();
		List<Room> availableRooms = roomService.getAvailableRooms();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", availableRooms);
		model.addAttribute("guests", guests);

		List<Room> rooms = roomService.getAllRooms();
		model.addAttribute("booking", new Booking());
		model.addAttribute("rooms", rooms);
		model.addAttribute("occupiedRooms", occupiedRooms);

		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		return "rooms/change-room";
	}

	@PostMapping("/change-room")
	public String changeRoom(@RequestParam(required = false) Long guestId, @RequestParam Long newRoomId,
			RedirectAttributes redirectAttributes) {
		try {
			Optional<Booking> bookingOptional = bookingRepository.findBookingByGuestId(guestId);

			if (bookingOptional.isPresent()) {
				Booking booking = bookingOptional.get();
				Long bookingId = booking.getId();

				System.out.println("Received Booking ID: " + bookingId + ", New Room ID: " + newRoomId);

				bookingService.changeRoom(bookingId, newRoomId);

				redirectAttributes.addFlashAttribute("message", "Room successfully changed.");
			} else {
				redirectAttributes.addFlashAttribute("error", "No booking found for the given guest ID.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error changing room: " + e.getMessage());
		}

		return "redirect:/rooms/change-room";
	}

	// -----------------------------------

	@GetMapping("/view-rooms")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'RECEPTIONIST')")
	public String viewRooms(Model model, Principal principal) {

		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);

		List<Integer> floors = roomRepository.findDistinctFloors();
		List<Room> allRooms = roomRepository.findAll();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", allRooms);
		model.addAttribute("guests", guests);
		return "rooms/available-rooms";
	}

	// ------------------------------------

	@GetMapping("/get-booking-id-by-guest")
	@ResponseBody
	public Long getBookingIdByGuest(@RequestParam("guestId") Long guestId) {
		return bookingService.getBookingByGuestId(guestId).map(Booking::getId).orElse(null);
	}

	// ---------------- Render Floor --------------------//


	@GetMapping("/floors")
	public ResponseEntity<List<Integer>> getFloors() {
		return ResponseEntity.ok(roomService.getUniqueFloors());
	}

	// ----------------- End ------------------------------//

	@GetMapping("/occupancy")
	public ResponseEntity<Map<String, Integer>> getRoomOccupancy() {
		int totalRooms = roomService.getTotalRooms();
		int occupiedRooms = roomService.getOccupiedRoomsChart();
		int availableRooms = roomService.getAvailableRoomsChart();

		Map<String, Integer> data = new HashMap<>();
		data.put("Total Rooms", totalRooms);
		data.put("Occupied Rooms", occupiedRooms);
		data.put("Available Rooms", availableRooms);

		System.out.println(data);
		return ResponseEntity.ok(data);
	}

}
