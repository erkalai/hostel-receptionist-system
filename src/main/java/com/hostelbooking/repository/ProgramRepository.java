package com.hostelbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long>{

	 @Query("SELECT p FROM Program p ORDER BY p.isStarred DESC, p.name ASC")
	    List<Program> findAllOrderedByStarred();
}
