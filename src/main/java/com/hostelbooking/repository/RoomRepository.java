package com.hostelbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByCapacityGreaterThan(int capacity);

	Optional<Room> findByRoomNumberAndFloor(String roomNumber, int floor);

	@Query("SELECT r FROM Room r WHERE r.capacity < r.maxCapacity")
	List<Room> getOccupiedRooms();

	// ---------------- Render --------------//

	@Query("SELECT DISTINCT r.floor FROM Room r")
	List<Integer> findDistinctFloors();

	// -----------------End -----------------//

	@Query("SELECT COUNT(r) FROM Room r")
	int countAllRooms();

	int countByCapacityGreaterThan(int capacity);

//	    @Query("SELECT r FROM Room r WHERE r.gender = 'male' AND r.capacity > 0")
//	    List<Room> getMaleOccupiedRooms();
//	    
	@Query("SELECT r FROM Room r JOIN r.guests g WHERE g.gender = :gender AND r.capacity > 0 AND SIZE(r.guests) > 0")
	List<Room> getOccupiedRoomsByGender(@Param("gender") String gender);

}
