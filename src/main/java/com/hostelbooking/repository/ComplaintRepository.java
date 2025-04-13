package com.hostelbooking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	
	
	List<Complaint> findByStatus(String status);

	
	 long countByStatus(Complaint.Status status);
	 

	 
	 
	 //--------------------Filter
	 
	 public List<Complaint> findByStatus(Complaint.Status status);
	 public List<Complaint> findByCreatedDateBetween(LocalDate start, LocalDate end);
	 public List<Complaint> findByStatusAndCreatedDateBetween(Complaint.Status status, LocalDate start, LocalDate end);
	 public List<Complaint> findByCreatedDateAfter(LocalDate start);
	 public List<Complaint> findByCreatedDateBefore(LocalDate end);

	 public List<Complaint> findByStatusAndCreatedDateAfter(Complaint.Status status, LocalDate startDate);
	 public List<Complaint> findByStatusAndCreatedDateBefore(Complaint.Status status, LocalDate endDate);

	 
}

