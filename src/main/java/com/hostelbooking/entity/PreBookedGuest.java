package com.hostelbooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PreBookedGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "name")
    private String name;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "kid", unique = true)
    private String kid;

    @Column(name = "status")
    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PreBookedGuest(Long id, String programName, String name, String idType, String idNumber, String mobileNumber,
			String kid, String status) {
		super();
		this.id = id;
		this.programName = programName;
		this.name = name;
		this.idType = idType;
		this.idNumber = idNumber;
		this.mobileNumber = mobileNumber;
		this.kid = kid;
		this.status = status;
	}

	public PreBookedGuest() {
	}


    
    
    
    
}

