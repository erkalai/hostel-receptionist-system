package com.hostelbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.PreBookedGuest;

@Repository
public interface PreBookedGuestRepository extends JpaRepository<PreBookedGuest, Long> {

//	@Query("SELECT p FROM PreBookedGuest p WHERE p.kid = :kid")
//	Optional<PreBookedGuest> findByKid(@Param("kid") String kid);
	
	@Query(value = "SELECT * FROM pre_booked_guest WHERE kid = :kid", nativeQuery = true)
	Optional<PreBookedGuest> findByKid(@Param("kid") String kid);

	
	@Query(value = "SELECT * FROM pre_booked_guest WHERE kid = :kid", nativeQuery = true)
	PreBookedGuest findByKidA(@Param("kid") String kid);

}
