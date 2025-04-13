package com.hostelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.enums.GuestStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	Optional<Booking> findByGuestIdAndStatusInOrderByStatusAsc(Long guestId, List<GuestStatus> statuses);
	
	@Query("SELECT b.guest FROM Booking b WHERE b.room.id = :roomId AND b.status = 'CHECK_IN'	")
	List<Guest> findGuestsByRoomIdAndStatus(@Param("roomId") Long roomId);
	
	@Query("SELECT b FROM Booking b WHERE b.guest.id = :guestId")
	Optional<Booking> findBookingByGuestId(Long guestId);

	
	   @Query(value = "SELECT COUNT_BIG(b.id) FROM booking b WHERE b.status = :status AND CAST(b.check_in_date AS DATE) = CAST(GETDATE() AS DATE)", nativeQuery = true)
	    long countBookingsByStatusAndToday(@Param("status") String status);

	    @Query(value = "SELECT COUNT_BIG(b.id) FROM booking b WHERE b.status = :status AND MONTH(b.check_in_date) = :month", nativeQuery = true)
	    long countBookingsByStatusAndMonth(@Param("status") String status, @Param("month") int month);

	    @Query(value = "SELECT COUNT_BIG(b.id) FROM booking b WHERE b.status = :status AND YEAR(b.check_in_date) = :year", nativeQuery = true)
	    long countBookingsByStatusAndYear(@Param("status") String status, @Param("year") int year);

	
	// -------------------------------- New Report -----------------------//

		@Query("SELECT b.id, g.name, r.roomNumber, b.checkInDate, b.checkInTime, b.checkOutDate, b.checkOutTime, b.status "
				+ "FROM Booking b " + "JOIN b.guest g " + "JOIN b.room r " + "WHERE " + "(b.status = 'CHECK_IN' "
				+ "OR (b.status = 'CHECK_OUT' AND :startDate IS NOT NULL AND :endDate IS NOT NULL)) "
				+ "AND (:startDate IS NULL OR b.checkInDate >= :startDate OR b.checkOutDate >= :startDate) "
				+ "AND (:endDate IS NULL OR b.checkInDate <= :endDate OR b.checkOutDate <= :endDate) "
				+ "AND (:month IS NULL OR FUNCTION('MONTH', b.checkInDate) = :month OR FUNCTION('MONTH', b.checkOutDate) = :month) "
				+ "AND (:year IS NULL OR FUNCTION('YEAR', b.checkInDate) = :year OR FUNCTION('YEAR', b.checkOutDate) = :year)")
		List<Object[]> filterBookings(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
				@Param("month") Integer month, @Param("year") Integer year);

		// -------------------------------- End Report -----------------------//
	
	//---------------- Chart
		
		 // Count daily check-ins (for today)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKIN' AND FUNCTION('DATE', b.checkInDate) = CURRENT_DATE")
	    long countDailyCheckins();

	    // Count daily check-outs (for today)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKOUT' AND FUNCTION('DATE', b.checkOutDate) = CURRENT_DATE")
	    long countDailyCheckouts();

	    // Count monthly check-ins (for a specific month and year)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKIN' AND MONTH(b.checkInDate) = :month AND YEAR(b.checkInDate) = :year")
	    long countMonthlyCheckins(@Param("month") int month, @Param("year") int year);

	    // Count monthly check-outs (for a specific month and year)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKOUT' AND MONTH(b.checkOutDate) = :month AND YEAR(b.checkOutDate) = :year")
	    long countMonthlyCheckouts(@Param("month") int month, @Param("year") int year);

	    // Count yearly check-ins (for a specific year)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKIN' AND YEAR(b.checkInDate) = :year")
	    long countYearlyCheckins(@Param("year") int year);

	    // Count yearly check-outs (for a specific year)
	    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CHECKOUT' AND YEAR(b.checkOutDate) = :year")
	    long countYearlyCheckouts(@Param("year") int year);
		
	//----------------------
		

	    
	
	
	// -------------- Report Get and Filter-----------  //
	
	
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	
//	 List<Booking> findByCheckInDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, GuestStatus status);
//	    List<Booking> findByCheckInDateAndStatus(LocalDate date, GuestStatus status);
//	    List<Booking> findByCheckInDate(LocalDate date);
//	    List<Booking> findByCheckOutDate(LocalDate date);
//
//	    
//	    
//	    List<Booking> findByStatusAndCheckInDateAfter(GuestStatus status, LocalDate startDate);
//
//	    List<Booking> findByStatusAndCheckOutDateBefore(GuestStatus status, LocalDate endDate);
//
//	    List<Booking> findByStatusAndCheckInDateBetween(GuestStatus status, LocalDate startDate, LocalDate endDate);
//
//	    List<Booking> findByCheckInDateAfter(LocalDate startDate);
//
//	    List<Booking> findByCheckOutDateBefore(LocalDate endDate);
//
//
//	    List<Booking> findByStatus(GuestStatus status);
//
//	    
//	    
//
//	    // Query to filter by check-in date range and status
//	    @Query("SELECT b FROM Booking b WHERE b.checkInDate BETWEEN :startDate AND :endDate AND b.status = :status AND b.guest.id = :guestId")
//	    List<Booking> findByCheckInDateBetweenAndStatusAndGuestId(
//	            @Param("startDate") LocalDate startDate,
//	            @Param("endDate") LocalDate endDate,
//	            @Param("status") GuestStatus status,
//	            @Param("guestId") Long guestId);
//
//	    // Query to filter by check-in date after a certain date and status
//	    @Query("SELECT b FROM Booking b WHERE b.checkInDate > :startDate AND b.status = :status AND b.guest.id = :guestId")
//	    List<Booking> findByCheckInDateAfterAndStatusAndGuestId(
//	            @Param("startDate") LocalDate startDate,
//	            @Param("status") GuestStatus status,
//	            @Param("guestId") Long guestId);
//
//	    // Query to filter by check-out date before a certain date and status
//	    @Query("SELECT b FROM Booking b WHERE b.checkOutDate < :endDate AND b.status = :status AND b.guest.id = :guestId")
//	    List<Booking> findByCheckOutDateBeforeAndStatusAndGuestId(
//	            @Param("endDate") LocalDate endDate,
//	            @Param("status") GuestStatus status,
//	            @Param("guestId") Long guestId);
//	    
//	    
//	    
//	    // Finds bookings by check-in date between the given range
//	    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);

	    // Retrieves all bookings
	    List<Booking> findAll();
	    // ------------------------ END -------------- //
	
	//---- New Filter-------------
	    @Query(value = "SELECT * FROM booking WHERE status = :status", nativeQuery = true)
	    List<Booking> findByStatus(@Param("status") GuestStatus guestStatus);

	    @Query(value = "SELECT * FROM booking WHERE check_in_date BETWEEN :start AND :end", nativeQuery = true)
	    List<Booking> findByCheckInDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

	    @Query(value = "SELECT * FROM booking WHERE status = :status AND check_in_date BETWEEN :start AND :end", nativeQuery = true)
	    List<Booking> findByStatusAndCheckInDateBetween(
	            @Param("status") GuestStatus guestStatus, 
	            @Param("start") LocalDate start, 
	            @Param("end") LocalDate end
	    );

	    @Query(value = "SELECT * FROM booking WHERE check_in_date > :start", nativeQuery = true)
	    List<Booking> findByCheckInDateAfter(@Param("start") LocalDate start);

	    @Query(value = "SELECT * FROM booking WHERE check_in_date < :end", nativeQuery = true)
	    List<Booking> findByCheckInDateBefore(@Param("end") LocalDate end);

	    @Query(value = "SELECT * FROM booking WHERE status = :status AND check_in_date > :start", nativeQuery = true)
	    List<Booking> findByStatusAndCheckInDateAfter(
	            @Param("status") GuestStatus guestStatus, 
	            @Param("start") LocalDate start
	    );

	    @Query(value = "SELECT * FROM booking WHERE status = :status AND check_in_date < :end", nativeQuery = true)
	    List<Booking> findByStatusAndCheckInDateBefore(
	            @Param("status") GuestStatus guestStatus, 
	            @Param("end") LocalDate end
	    );
	
	
	
	
//    List<Booking> findByRoom(Room room);
//    
//    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate AND b.checkInDate <= :endDate")
//    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//    
// // Find all bookings by guest
//    Optional<Booking> findByGuest(Guest guest);
//
//    // Find a booking by guest
//    Optional<Booking> findByGuestAndStatus(Guest guest, Booking.BookingStatus status);
//
//    // Find all bookings by status
//    List<Booking> findByStatus(Booking.BookingStatus status);
//
//    // Find bookings by room
//    List<Booking> findByRoomId(Long roomId);
//
//    // Find all bookings within a specific date range (for example, check-in date)
//    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
//    
//    Optional<Booking> findByGuestIdAndRoomIdAndStatus(Long guestId, Long roomId, Booking.BookingStatus status);
//    
//    Optional<Booking> findByGuestAndRoom(Guest guest, Room room);
}
