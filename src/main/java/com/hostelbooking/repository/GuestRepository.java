package com.hostelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
	
	

	
	
	
	
	
//	 Optional<Guest> findByMobileNumberAndKid(String mobileNumber, String kid);
//    
//    Guest findGuestById(Long id);
//
//	List<Guest> findByRoomId(Long id);
//	
//	List<Guest> findByRoom_RoomNumber(int roomNumber);
//	
//	   List<Guest> findByCheckInDate(LocalDate checkInDate);
//
//	    List<Guest> findByCheckOutDate(LocalDate checkOutDate);

//	    List<Guest> findByIdNumber(String idNumber);

//	    @Query("SELECT g FROM Guest g WHERE MONTH(g.checkInDate) = :month AND YEAR(g.checkInDate) = :year")
//	    List<Guest> findByCheckInDateMonthAndYear(@Param("month") int month, @Param("year") int year);
	    
//	    List<Guest> findByStatus(GuestStatus status);
}
