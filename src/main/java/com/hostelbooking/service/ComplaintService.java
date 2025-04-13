package com.hostelbooking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Complaint;
import com.hostelbooking.repository.ComplaintRepository;

import jakarta.transaction.Transactional;

@Service
public class ComplaintService {
	@Autowired
    private ComplaintRepository complaintRepository;

	@Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }
	
	@Transactional
    public Complaint saveComplaint(Complaint complaint) {
        try {
            System.out.println("Saving complaint: " + complaint);
            return complaintRepository.save(complaint);
        } catch (Exception e) {
            System.err.println("Error saving complaint: " + e.getMessage());
            throw e;
        }
    }

	 // Method to count the number of pending complaints
    public long countPendingComplaints() {
        return complaintRepository.countByStatus(Complaint.Status.PENDING);
    }
    
	 // Method to fetch complaints with status PENDING
    public List<Complaint> getPendingComplaints() {
        return complaintRepository.findByStatus("PENDING");
    }

    // Method to retrieve all complaints
    public List<Complaint> findAllComplaints() {
        return complaintRepository.findAll();
    }
    
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Complaint not found"));
    }
    
    public List<Complaint> findComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(Complaint.Status.valueOf(status));
    }

	
    
    //-----------------------Filter
    
    public List<Complaint> findComplaintsWithFilters(String status, String startDate, String endDate) {
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        // If status is provided
        if (status != null && !status.isEmpty()) {
            if (start != null && end != null) {
                return complaintRepository.findByStatusAndCreatedDateBetween(Complaint.Status.valueOf(status), start, end);
            } else if (start != null) {
                return complaintRepository.findByStatusAndCreatedDateAfter(Complaint.Status.valueOf(status), start);
            } else if (end != null) {
                return complaintRepository.findByStatusAndCreatedDateBefore(Complaint.Status.valueOf(status), end);
            } else {
                return complaintRepository.findByStatus(Complaint.Status.valueOf(status)); // No date filters
            }
        } else {
            // If no status is provided, filter by date range
            if (start != null && end != null) {
                return complaintRepository.findByCreatedDateBetween(start, end);
            } else if (start != null) {
                return complaintRepository.findByCreatedDateAfter(start);
            } else if (end != null) {
                return complaintRepository.findByCreatedDateBefore(end);
            } else {
                return complaintRepository.findAll(); // No filters, return all complaints
            }
        }
    }


    
    
}
