package com.hostelbooking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hostelbooking.enums.GuestStatus;

public class BookingReportDTO {
	private Long bookingId;
	private String guestName;
	private String roomNumber;
	private LocalDate checkInDate;
	private LocalDateTime checkInTime;
	private LocalDate checkOutDate;
	private LocalDateTime checkOutTime;
	private GuestStatus status;

	// Constructor
	public BookingReportDTO(Long bookingId, String guestName, String roomNumber, LocalDate checkInDate,
			LocalDateTime checkInTime, LocalDate checkOutDate, LocalDateTime checkOutTime, GuestStatus status) {
		this.bookingId = bookingId;
		this.guestName = guestName;
		this.roomNumber = roomNumber;
		this.checkInDate = checkInDate;
		this.checkInTime = checkInTime;
		this.checkOutDate = checkOutDate;
		this.checkOutTime = checkOutTime;
		this.status = status;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public LocalDateTime getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(LocalDateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public GuestStatus getStatus() {
		return status;
	}

	public void setStatus(GuestStatus status) {
		this.status = status;
	}

	public BookingReportDTO() {
	}

}
