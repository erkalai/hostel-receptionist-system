package com.hostelbooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Room;
import com.hostelbooking.repository.RoomRepository;

import jakarta.transaction.Transactional;

import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GuestRepository guestRepository;	
    
    
    public List<Room> getAvailableRooms() {
        return roomRepository.findByCapacityGreaterThan(0);
    }
    
    public List<Room> getOccupiedRooms(){
    return roomRepository.getOccupiedRooms();
    }
    
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    //Create Room By Admin
    public Room createRoom(Room room) {
        // Check if room with the same number exists on the same floor
        Optional<Room> existingRoom = roomRepository.findByRoomNumberAndFloor(room.getRoomNumber(), room.getFloor());
        if (existingRoom.isPresent()) {
            throw new IllegalArgumentException("Room " + room.getRoomNumber() + " already exists on floor " + room.getFloor());
        }

        // Check if capacity exceeds max capacity
        if (room.getCapacity() > room.getMaxCapacity()) {
            throw new IllegalArgumentException("Capacity cannot exceed max capacity.");
        }

        return roomRepository.save(room);
    }


    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setFloor(roomDetails.getFloor());
        room.setCapacity(roomDetails.getCapacity());
        room.setMaxCapacity(roomDetails.getMaxCapacity());
        return roomRepository.save(room);  // Save the updated room to the database
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    
    
    
    @Transactional
    public Room updateRoomCapacity(Long roomId, int change) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        int newCapacity = room.getCapacity() + change;

        // Validate new capacity
        if (newCapacity < 0) {
            throw new RuntimeException("Cannot reduce capacity below zero for room ID: " + roomId);
        }
        if (newCapacity > room.getMaxCapacity()) {
            throw new RuntimeException("Cannot exceed maximum capacity for room ID: " + roomId);
        }

        room.setCapacity(newCapacity);
        return roomRepository.save(room);
    }

    
    // -------------- Render Foor 
    
    public List<Integer> getUniqueFloors() {
        return roomRepository.findDistinctFloors();
    }
    
    
    
    //--------------End
    
 // Count total rooms
    public int getTotalRooms() {
        return (int) roomRepository.count();
    }

    // Count occupied rooms (assuming a room is occupied if capacity > 0)
    public int getOccupiedRoomsChart() {
        return (int) roomRepository.countByCapacityGreaterThan(0);
    }

    // Count available rooms (total rooms - occupied rooms)
    public int getAvailableRoomsChart() {
        return getTotalRooms() - getOccupiedRoomsChart();
    }

    // Find By Gender
	public List<Room> getAvailableRoomsByGender(String gender) {
		
		return roomRepository.getOccupiedRoomsByGender(gender);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    @Transactional
//    public Room createRoom(Room room) throws Exception{
//    	if (roomRepository.findByRoomNumber(room.getRoomNumber()) != null) {
//            throw new Exception("Room number " + room.getRoomNumber() + " already exists!");
//        }
//        return roomRepository.save(room);
//    }

    
    
//
//    public Optional<Room> findById(Long roomId) {
//        return roomRepository.findById(roomId);
//    }
//    
//
//    
//    // Method to find room by room number
//    public Room getRoomByNumber(int roomNumber) {
//        return roomRepository.findByRoomNumber(roomNumber);
//    }
//
//    public Room getRoomWithGuests(Long roomId) {
//        // Use the repository to fetch the room with guests
//        return roomRepository.findRoomWithGuestsById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));
//    }
    
    
//    @Transactional
//    public void updateRoomAvailability(Long roomId, boolean isAvailable) {
//        Room room = roomRepository.findById(roomId).orElse(null);
//        if (room != null) {
//            room.setIsAvailable(isAvailable);
//            roomRepository.save(room);
//        }
//    }
//
//    // Fetch available rooms (only those marked as available)
//    public List<Room> getAvailableRoom() {
//        return roomRepository.findByIsAvailable(true);
//    }


//    @Transactional
//    public void createBooking(Booking booking) throws Exception {
//        Room room = booking.getRoom();
//        
//        // Check if the room is available
//        if (room == null || !room.isAvailable()) {
//            throw new Exception("Room is not available");
//        }
//
//        // Mark the room as unavailable
//        room.setIsAvailable(false);
//        roomRepository.save(room);  // Save the room status
//
//        // Save the booking to the database
//        booking.setStatus(Booking.BookingStatus.CHECK_IN);  // Set initial status to CHECK_IN
//        bookingRepository.save(booking);  // Save the booking
//    }
//    
//    @Transactional
//    public Room saveRoom(Room room) {
//        return roomRepository.save(room);
//    }
//
//    public Room getRoomDetails(int roomNumber) {
//        return roomRepository.findByRoomNumber(roomNumber);
//    }
//    
// // Method to checkout guests
//    public void checkoutGuests(List<Long> guestIds) {
//        List<Guest> guests = guestRepository.findAllById(guestIds);
//        for (Guest guest : guests) {
//            guest.setStatus(Guest.GuestStatus.CHECKED_OUT); // Mark as checked out
//            guest.setCheckOutDate(LocalDate.now()); // Set today's date as checkout date
//            guest.setCheckOutTime(LocalTime.now()); // Set current time as checkout time
//        }
//        guestRepository.saveAll(guests);
//    }
    
    
    
    
//    Last update
//    
//
//
//    public Room getRoomDetails(Long roomId) {
//        return roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
//    }

//    public void checkoutGuest(Long guestId, Long roomId) {
//        // Fetch room and guest
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
//        Guest guest = guestRepository.findById(guestId)
//                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));
//
//        // Set guest status to "CHECKED_OUT" using the String input
//        guest.setStatus("CHECKED_OUT");
//        guestRepository.save(guest);
//
//        // Remove guest from the room's guest list and update room availability
//        room.getGuests().remove(guest);
//        room.setIsAvailable(room.getGuests().isEmpty());
//        roomRepository.save(room);
//    }
    
}
