package com.hostelbooking.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hostelbooking.component.ExcelHelper;
import com.hostelbooking.entity.PreBookedGuest;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.PreBookedGuestRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GuestService {

	private final GuestRepository guestRepository;

	private final BookingRepository bookingRepository;

	private final PreBookedGuestRepository preBookedGuestRepository;

	@Autowired
	public GuestService(GuestRepository guestRepository, BookingRepository bookingRepository,
			PreBookedGuestRepository preBookedGuestRepository) {
		this.preBookedGuestRepository = preBookedGuestRepository;
		this.guestRepository = guestRepository;
		this.bookingRepository = bookingRepository;
	}

	public List<PreBookedGuest> saveGuestsFromExcel(MultipartFile file) {
		System.out.println("***************** Service");
		try {
			List<PreBookedGuest> guests = ExcelHelper.excelToGuests(file.getInputStream());
			System.out.println("***************** Service Try");
			preBookedGuestRepository.saveAll(guests);
			return guests;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to store Excel data: " + e.getMessage());
		}
	}

}
