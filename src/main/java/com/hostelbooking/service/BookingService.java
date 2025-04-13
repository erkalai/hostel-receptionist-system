	package com.hostelbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.dto.BookingReportDTO;
import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.Room;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.GuestStatus;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private final RoomService roomService;
    private UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, RoomRepository roomRepository, GuestRepository guestRepository,RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.roomService = roomService;
        this.userRepository = userRepository;
    }
    
  

    @Transactional
	public Booking checkIn(Guest guest, Long roomId, Long userId) {

		System.out.println("Guest details: " + guest.getName() + ", Room ID: " + roomId + ", User ID: " + userId);

		Room room = roomService.updateRoomCapacity(roomId, -1);

		System.out.println("************************************** : After Room" + room);
		String programName = guest.getProgramName();
		System.out.println("Program Name: " + programName);

//        Program program = programRepository.findByNameNative(guest.getProgramName())
//                .orElseThrow(() -> new RuntimeException("Program not found: " + guest.getProgramName()));
//
//
//
//        guest.setProgram(program);

		guest.setRoom(room);
		Guest savedGuest = guestRepository.save(guest);
		System.out.println("Guest saved with ID: " + savedGuest.getId());

		User user;
		try {
			user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			System.out.println("User found with ID: " + user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching user: " + e.getMessage());
		}

		// Create a new booking
		Booking booking = new Booking();
		booking.setGuest(savedGuest);
		booking.setRoom(room);
		booking.setCheckInDate(LocalDate.now());
		booking.setCheckInTime(LocalDateTime.now());
		booking.setStatus(GuestStatus.CHECK_IN);
		booking.setCheckInUser(user);

		return bookingRepository.save(booking);
	}
    
	@Transactional
	public Booking checkOut(Long guestId, Long userId) {
		List<GuestStatus> statuses = List.of(GuestStatus.CHECK_IN);
		Booking booking = bookingRepository.findByGuestIdAndStatusInOrderByStatusAsc(guestId, statuses)
				.orElseThrow(() -> new RuntimeException("Active booking not found for the guest"));

		roomService.updateRoomCapacity(booking.getRoom().getId(), 1);

		booking.setCheckOutDate(LocalDate.now());
		booking.setCheckOutTime(LocalDateTime.now());
		booking.setStatus(GuestStatus.CHECK_OUT);

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		booking.setCheckOutUser(user);

		return bookingRepository.save(booking);
	}

    
    
    @Transactional
    public void changeRoom(Long bookingId, Long newRoomId) {
        // Fetch the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        System.out.println("Fetched Booking: " + booking);

        // Free the old room
        Long oldRoomId = booking.getRoom().getId();
        roomService.updateRoomCapacity(oldRoomId, 1);
        System.out.println("Freed Old Room ID: " + oldRoomId);

        // Allocate the new room
        Room newRoom = roomService.updateRoomCapacity(newRoomId, -1);
        System.out.println("Allocated New Room ID: " + newRoomId);

        // Update the booking with the new room
        booking.setRoom(newRoom);
        booking.setStatus(GuestStatus.CHECK_IN);
        

        // Save the updated booking
        bookingRepository.save(booking);
        System.out.println("Updated Booking Saved: " + booking);
        
        
        // Update the guest table with the new room (assuming guest has a reference to the room)
        Guest guest = booking.getGuest();  // Get the guest associated with the booking
        guest.setRoom(newRoom);  // Update the guest's room
        guestRepository.save(guest);  // Save the updated guest entity
        System.out.println("Updated Guest with New Room: " + guest);
    }
    
    
    public Optional<Booking> getBookingByGuestId(Long guestId) {
        return bookingRepository.findBookingByGuestId(guestId);
    }
    
    
    
 // ------------- Report Start --------------- //

 	public List<BookingReportDTO> getFilteredBookings(LocalDate startDate, LocalDate endDate, Integer month,
 			Integer year) {
 		List<Object[]> result = bookingRepository.filterBookings(startDate, endDate, month, year);
 		return result.stream().map(objects -> new BookingReportDTO((Long) objects[0], // bookingId
 				(String) objects[1], (String) objects[2], (LocalDate) objects[3], (LocalDateTime) objects[4],
 				(LocalDate) objects[5], (LocalDateTime) objects[6], (GuestStatus) objects[7]))
 				.collect(Collectors.toList());
 	}

 	// ------------------ End -------------------------//

    
    
    //------------- Chart ------------------------------------
 	
 	
 	 public Map<String, Long> getDailyCounts() {
         long checkins = bookingRepository.countBookingsByStatusAndToday("CHECK_IN");
         long checkouts = bookingRepository.countBookingsByStatusAndToday("CHECK_OUT");

         Map<String, Long> counts = new HashMap<>();
         counts.put("checkins", checkins);
         counts.put("checkouts", checkouts);
         return counts;
     }

     public List<Map<String, Object>> getMonthlyCounts() {
         List<Map<String, Object>> monthlyCounts = new ArrayList<>();
         for (int i = 1; i <= 12; i++) {
             long checkins = bookingRepository.countBookingsByStatusAndMonth("CHECK_IN", i);
             long checkouts = bookingRepository.countBookingsByStatusAndMonth("CHECK_OUT", i);
             Map<String, Object> counts = new HashMap<>();
             counts.put("month", i);
             counts.put("checkins", checkins);
             counts.put("checkouts", checkouts);
             monthlyCounts.add(counts);
         }
         return monthlyCounts;
     }

     public List<Map<String, Object>> getYearlyCounts() {
         List<Map<String, Object>> yearlyCounts = new ArrayList<>();
         int currentYear = LocalDate.now().getYear();
         for (int i = currentYear - 4; i <= currentYear; i++) {
             long checkins = bookingRepository.countBookingsByStatusAndYear("CHECK_IN", i);
             long checkouts = bookingRepository.countBookingsByStatusAndYear("CHECK_OUT", i);
             Map<String, Object> counts = new HashMap<>();
             counts.put("year", i);
             counts.put("checkins", checkins);
             counts.put("checkouts", checkouts);
             yearlyCounts.add(counts);
         }
         return yearlyCounts;
     }
 	
 	
 	//---------------- End
    
    
    
    //----------Report

     public List<Booking> findBookingsWithFilters(String status, String startDate, String endDate) {
    	    LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
    	    LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

    	    GuestStatus guestStatus = null;
    	    if (status != null && !status.isEmpty()) {
    	        try {
    	            guestStatus = GuestStatus.valueOf(status.trim().toUpperCase());
    	        } catch (IllegalArgumentException e) {
    	            throw new IllegalArgumentException("Invalid status: " + status + ". Must be CHECK_IN or CHECK_OUT.");
    	        }
    	    }

    	    if (guestStatus != null) {
    	        if (start != null && end != null) {
    	            return bookingRepository.findByStatusAndCheckInDateBetween(guestStatus, start, end);
    	        } else if (start != null) {
    	            return bookingRepository.findByStatusAndCheckInDateAfter(guestStatus, start);
    	        } else if (end != null) {
    	            return bookingRepository.findByStatusAndCheckInDateBefore(guestStatus, end);
    	        } else {
    	            return bookingRepository.findByStatus(guestStatus);
    	        }
    	    } else {
    	        if (start != null && end != null) {
    	            return bookingRepository.findByCheckInDateBetween(start, end);
    	        } else if (start != null) {
    	            return bookingRepository.findByCheckInDateAfter(start);
    	        } else if (end != null) {
    	            return bookingRepository.findByCheckInDateBefore(end);
    	        } else {
    	            return bookingRepository.findAll();
    	        }
    	    }
    	}


     
     
     
//     public List<Booking> findBookingsWithFilters(String status, String startDate, String endDate) {
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//         LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate, formatter) : null;
//         LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate, formatter) : null;
//
//         if (status != null && !status.isEmpty()) {
//             if (start != null && end != null) {
//                 return bookingRepository.findByStatusAndCheckInDateBetween(GuestStatus.valueOf(status), start, end);
//             } else if (start != null) {
//                 return bookingRepository.findByStatusAndCheckInDateAfter(GuestStatus.valueOf(status), start);
//             } else if (end != null) {
//                 return bookingRepository.findByStatusAndCheckOutDateBefore(GuestStatus.valueOf(status), end);
//             } else {
//                 return bookingRepository.findByStatus(GuestStatus.valueOf(status));  // No date filters
//             }
//         } else {
//             // If no status is provided, filter by date range
//             if (start != null && end != null) {
//                 return bookingRepository.findByCheckInDateBetween(start, end);
//             } else if (start != null) {
//                 return bookingRepository.findByCheckInDateAfter(start);
//             } else if (end != null) {
//                 return bookingRepository.findByCheckOutDateBefore(end);
//             } else {
//                 return bookingRepository.findAll();  // No filters, return all bookings
//             }
//         }
//     }
//    
    
    
    
    
    
    

    
    
}
