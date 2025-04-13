package com.hostelbooking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;


@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    private String roomNumber;
    private String phoneNumber;

    @Lob
    private String message;

    // Add an Enum for status
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDate createdDate;
    private LocalDate completedDate;
    
    private String completedDescription;
    
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();  // Automatically set createdDate when the entity is persisted
    }
    // Enum for status: PENDING or COMPLETED
    public enum Status {
        PENDING, COMPLETED
    }

    // Default no-args constructor for JPA and Spring
    public Complaint() {
        this.status = Status.PENDING; // default to "PENDING" status when complaint is created
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(LocalDate completedDate) {
		this.completedDate = completedDate;
	}

	public String getCompletedDescription() {
		return completedDescription;
	}

	public void setCompletedDescription(String completedDescription) {
		this.completedDescription = completedDescription;
	}

	@Override
	public String toString() {
		return "Complaint [id=" + id + ", name=" + name + ", roomNumber=" + roomNumber + ", phoneNumber=" + phoneNumber
				+ ", message=" + message + ", status=" + status + ", createdDate=" + createdDate + ", completedDate="
				+ completedDate + ", completedDescription=" + completedDescription + "]";
	}

	public Complaint(Long id, @NotNull(message = "Name cannot be null") String name, String roomNumber,
			String phoneNumber, String message, Status status, LocalDate createdDate, LocalDate completedDate,
			String completedDescription) {
		super();
		this.id = id;
		this.name = name;
		this.roomNumber = roomNumber;
		this.phoneNumber = phoneNumber;
		this.message = message;
		this.status = status;
		this.createdDate = createdDate;
		this.completedDate = completedDate;
		this.completedDescription = completedDescription;
	}

    
  

}
