package com.hostelbooking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Guest;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;
    
    private final BookingRepository bookingRepository;

    @Autowired
    public GuestService(GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

 

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
//    public Optional<Guest> findGuestByMobileNumberAndKid(String mobileNumber, String kid) {
//        return guestRepository.findByMobileNumberAndKid(mobileNumber, kid);
//    }
//    
//    @Transactional
//    public Guest saveGuest(Guest guest) {
//        return guestRepository.save(guest);
//    }
//    
//    @Transactional
//    public void updateGuestStatus(Guest guest, GuestStatus status) {
//        guest.setStatus(status);
//        guestRepository.save(guest); 
//    }
    
    
 // Find guest by ID
//    public Optional<Guest> findGuestById(Long guestId) {
//        return guestRepository.findById(guestId);
//    }
//
//    @Transactional
//    public void deleteGuest(Long guestId) {
//        guestRepository.deleteById(guestId);
//    }
//    
//    public Guest updateGuestStatusForCheckIn(Guest guest) {
//        guest.setStatus(Guest.GuestStatus.CHECKED_IN);
//        return guestRepository.save(guest);
//    }
////    
//    public List<Guest> getGuestsByRoomNumber(int roomNumber) {
//        return guestRepository.findByRoom_RoomNumber(roomNumber);
//    }
    
    
    // Fetch guests by room number
//    public List<Guest> getGuestsByRoom(int roomNumber) {
//        return guestRepository.findByRoomNumber(roomNumber);  // Assume you have a method in the repository for this
//    }

    // Fetch guest details by guest ID
//    public Guest getGuestDetails(Long guestId) {
//        return guestRepository.findById(guestId).orElse(null);
//    }
//    
////    Guest List
//    
//    public List<Guest> getAllGuests() {
//        return guestRepository.findAll();
//    }
////
//
//    public List<Guest> filterGuestsByIdNumber(String idNumber) {
//        return guestRepository.findByIdNumber(idNumber);
//    }
//
//    public List<Guest> filterGuestsByMonthAndYear(int month, int year) {
//        return guestRepository.findByCheckInDateMonthAndYear(month, year);
//    }
//    
//    public List<Guest> filterGuestsByStatus(GuestStatus status) {
//        return guestRepository.findByStatus(status);
//    }
    
    
}
