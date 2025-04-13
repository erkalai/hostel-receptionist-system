package com.hostelbooking.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hostelbooking.entity.Room;
import com.hostelbooking.entity.User;
import com.hostelbooking.service.RoomService;
import com.hostelbooking.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminRoomController {
	private final RoomService roomService;

	@Autowired
	private UserService userService;

	@Autowired
	public AdminRoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	// ---------- Show Manage Rooms
	@GetMapping("/manage-rooms")
	public String showRoomPage(Model model, Principal principal) {
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		model.addAttribute("room", new Room());
		model.addAttribute("rooms", roomService.getAllRooms());
		return "admin/rooms";
	}

	// ----------Create a new room
	@PostMapping("/create-room")
	public String createRoom(@ModelAttribute Room room, Model model) {
		try {
			roomService.createRoom(room);
			model.addAttribute("successMessage", "Room created successfully!");
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error creating room: " + e.getMessage());
		}
		return "redirect:/admin/manage-rooms";
	}

	// Edit an existing room -fetch room data
	@GetMapping("/edit-room/{id}")
	public String showEditRoomPage(@PathVariable Long id, Model model, Principal principal) {
		User user = userService.findUserByEmail(principal.getName());

		model.addAttribute("userName", user);
		Optional<Room> room = roomService.getRoomById(id);
		model.addAttribute("rooms", roomService.getAllRooms());
		if (room.isPresent()) {
			model.addAttribute("room", room.get());
			return "admin/rooms";
		} else {
			model.addAttribute("errorMessage", "Room not found");
			return "redirect:/admin/rooms";
		}

	}

	// Update room details
	@PostMapping("/edit-room/{id}")
	public String updateRoom(@PathVariable Long id, @ModelAttribute Room room, Model model) {
		try {
			roomService.updateRoom(id, room); // Update room in the database
			model.addAttribute("successMessage", "Room updated successfully!");
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error updating room: " + e.getMessage());
		}
		return "redirect:/admin/manage-rooms"; // Redirect after successful update
	}

	// Delete a room
	@GetMapping("/delete-room/{id}")
	public String deleteRoom(@PathVariable Long id, Model model) {
		try {
			roomService.deleteRoom(id);
			model.addAttribute("successMessage", "Room deleted successfully!");
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error deleting room: " + e.getMessage());
		}
		return "redirect:/admin/manage-rooms";
	}

//    @PostMapping("/create-room")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String createRoom(@ModelAttribute Room room, Model model) {
//        try {
//            roomService.createRoom(room);
//            model.addAttribute("availableRooms", roomService.getAvailableRoom());
//            model.addAttribute("successMessage", "Room created successfully!");
//            return "/admin/create-room";  // Redirect to the room creation page after success
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Room creation failed: " + e.getMessage());
//            return "admin/create-room";
//        }
//    }
}
