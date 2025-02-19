//package com.hostelbooking.controller;
//
//import java.security.Principal;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.hostelbooking.entity.Role;
//import com.hostelbooking.entity.User;
//import com.hostelbooking.service.UserService;
//
//import jakarta.servlet.http.HttpSession;
//
//
//public class LoginController {
//
//    @Autowired
//    private UserService userService;  // Service to fetch user details
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;  // Password Encoder (e.g., BCrypt)
//
//    @PostMapping("/login")
//    public String login(@RequestParam String username, 
//                        @RequestParam String password, 
//                        Model model, 
//                        HttpSession session, 
//                        RedirectAttributes redirectAttributes) {
//
//        // Find the user by email (username here is assumed to be email)
//        User user = userService.findUserByEmail(username);  // Fetching user by email
//
//        // Step 2: Check if the user exists and if the password matches
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//
//            // Step 3: Check if the user is deactivated
//            if (user.isActive()) {
//
//                // Step 4: Store user details in session (for example)
//                session.setAttribute("user", user);
//
//                if (user.getRoles() == null || user.getRoles().isEmpty()) {
//                    return "redirect:/login?error";
//                }
//
//                // Get the first role of the user
//                Role userRole = user.getRoles().stream().findFirst().orElse(null);
//
//                if (userRole == null) {
//                    return "redirect:/login";
//                }
//
//                // Step 5: Redirect to the appropriate dashboard based on role
//                switch (userRole.getName()) {
//                case ADMIN:
//                    return "/admin/dashboard";
//                case MODERATOR:
//                    return "/moderator/dashboard";
//                case RECEPTIONIST:
//                    return "/receptionist/dashboard";
//                default:
//                    return "redirect:/login";
//            }
//            } else {
//                // Step 6: User is deactivated
//                redirectAttributes.addFlashAttribute("error", "Your account is deactivated.");
//                return "redirect:/login";
//            }
//
//        } else {
//            // Invalid credentials
//            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
//            return "redirect:/login";
//        }
//    }
//
//}
