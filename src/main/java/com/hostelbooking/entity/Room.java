package com.hostelbooking.entity;



import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int floor;

    private String roomNumber;
    
    private int capacity;
    
    private int maxCapacity;


    @OneToMany(mappedBy = "room")
    private List<Guest> guests;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ",  floor=" + floor + ", roomNumber=" + roomNumber
				+ ", capacity=" + capacity + ", maxCapacity=" + maxCapacity + "]";
	}

	public Room(Long id, boolean isAvailable, int floor, String roomNumber, int capacity, int maxCapacity) {
		super();
		this.id = id;
		this.floor = floor;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
		this.maxCapacity = maxCapacity;
	}

	public Room() {
	}
	
    
    
}
