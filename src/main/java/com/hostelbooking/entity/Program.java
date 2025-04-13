package com.hostelbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private boolean isStarred = false;
    
    


	public boolean getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(boolean isStarred) {
		this.isStarred = isStarred;
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

	
	public Program(Long id, String name, boolean isStarred) {
		super();
		this.id = id;
		this.name = name;
		this.isStarred = isStarred;
	}

	@Override
	public String toString() {
		return "Program [id=" + id + ", name=" + name + ", isStarred=" + isStarred + "]";
	}

	public Program() {
	}
    
    
    
    
}