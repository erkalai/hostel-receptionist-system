package com.hostelbooking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hostelbooking.enums.GuestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "guest_id")
	    private Guest guest;

	    @ManyToOne
	    @JoinColumn(name = "room_id", referencedColumnName = "id")
	    private Room room;

	    private LocalDate checkInDate;
	    private LocalDateTime checkInTime;
	    private LocalDate checkOutDate;
	    private LocalDateTime checkOutTime;

	    @Enumerated(EnumType.STRING)
	    private GuestStatus status;

	    @ManyToOne
	    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	    private User user;
	    

	    @ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "checkin_user_id", referencedColumnName = "id")
		private User checkInUser;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "checkout_user_id", referencedColumnName = "id", nullable = true)
		private User checkOutUser;

		
		
		public User getCheckInUser() {
			return checkInUser;
		}

		public void setCheckInUser(User checkInUser) {
			this.checkInUser = checkInUser;
		}

		public User getCheckOutUser() {
			return checkOutUser;
		}

		public void setCheckOutUser(User checkOutUser) {
			this.checkOutUser = checkOutUser;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Guest getGuest() {
			return guest;
		}

		public void setGuest(Guest guest) {
			this.guest = guest;
		}

		public Room getRoom() {
			return room;
		}

		public void setRoom(Room room) {
			this.room = room;
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

		public void setCheckOutTime(LocalDateTime localTime) {
			this.checkOutTime = localTime;
		}

		public GuestStatus getStatus() {
			return status;
		}

		public void setStatus(GuestStatus status) {
			this.status = status;
		}

		



		public Booking(Long id, Guest guest, Room room, LocalDate checkInDate, LocalDateTime checkInTime,
				LocalDate checkOutDate, LocalDateTime checkOutTime, GuestStatus status, User user, User checkInUser,
				User checkOutUser) {
			super();
			this.id = id;
			this.guest = guest;
			this.room = room;
			this.checkInDate = checkInDate;
			this.checkInTime = checkInTime;
			this.checkOutDate = checkOutDate;
			this.checkOutTime = checkOutTime;
			this.status = status;
			this.user = user;
			this.checkInUser = checkInUser;
			this.checkOutUser = checkOutUser;
		}

		public Booking() {
		}
    
	    
	    
}
