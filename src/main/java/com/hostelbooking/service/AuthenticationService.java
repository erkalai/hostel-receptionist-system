package com.hostelbooking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.User;
import com.hostelbooking.repository.UserRepository;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean authenticate(String username, String password) {
		User user = userRepository.findByEmail(username);

		if (user != null) {
			return passwordEncoder.matches(password, user.getPassword());
		}

		return false;
	}
}
