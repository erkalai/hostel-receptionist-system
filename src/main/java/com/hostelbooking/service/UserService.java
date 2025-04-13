	package com.hostelbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hostelbooking.dto.UserDto;
import com.hostelbooking.entity.Role;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.RoleType;
import com.hostelbooking.repository.RoleRepository;
import com.hostelbooking.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
    private final UserRepository userRepository;
	@Autowired
    private final RoleRepository roleRepository;
	@Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveUser(UserDto userDto) {
        if (userDto == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("User details are incomplete");
        }

     // Check if the email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Log the details of userDto
        System.out.println("Saving user: " + userDto.getEmail());

        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Log the encrypted password
        System.out.println("Encrypted Password: " + user.getPassword());

        RoleType roleType = RoleType.valueOf(userDto.getRoleName().toUpperCase());
        Role role = roleRepository.findByName(roleType);
        
        
        if (role == null) {
            role = createRole(roleType);  // Create role if it doesn't exist
        }

        user.setRoles(List.of(role));
        
        userRepository.save(user);

        // Log user creation
        System.out.println("User saved successfully: " + user.getEmail());
    }

    @Transactional
    public void updateUserWithRoles(Long id, UserDto userDto) throws Throwable {
        // Prepare user details
        String fullName = userDto.getFirstName() + " " + userDto.getLastName();
        String updatedPassword = userDto.getPassword();

        // Encode password if provided
//        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
//            encodedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());
//        }

        // Update user details
        int rowsUpdated = userRepository.updateUserNative(id, fullName, userDto.getEmail(), updatedPassword);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        // Update roles
        userRepository.deleteUserRoles(id); // Clear existing roles

        Role role = roleRepository.findByName(RoleType.valueOf(userDto.getRoleName()));
        if (role == null) {
            throw new IllegalArgumentException("Invalid role name: " + userDto.getRoleName());
        }

        userRepository.addUserRole(id, role.getId()); // Assign the new role
    }






    public void activateUser(Long id) throws Throwable {
        User user = (User) userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    public void deactivateUser(Long id) throws Throwable {
        User user = (User) userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    

    private Role createRole(RoleType roleType) {
        // Create a new Role if it doesn't exist
        Role role = new Role();
        role.setName(roleType);  // Set role type based on enum
        return roleRepository.save(role);
    }
  

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findUserById(Long id) {
    	return userRepository.findById(id);
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(this::convertEntityToDto)
                    .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ", 2);  // Handle splitting of full name
        userDto.setFirstName(name[0]);
        userDto.setLastName(name.length > 1 ? name[1] : "");
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    
 // ----------------------- User profile Update Password

 	// Method to find user by username
 	public User findByUsername(String username) {
 		return userRepository.findByEmail(username);
 	}

 	// Method to check if the old password matches
 	public boolean checkPassword(User user, String oldPassword) {
 		return passwordEncoder.matches(oldPassword, user.getPassword());
 	}

 	// Method to update the password
 	public void updatePassword(User user, String newPassword) {
 		String encodedPassword = passwordEncoder.encode(newPassword);
 		user.setPassword(encodedPassword);
 		userRepository.save(user);
 	}
}
