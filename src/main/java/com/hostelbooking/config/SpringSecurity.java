package com.hostelbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

   
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .requestMatchers("/bookings/check-in").hasAnyRole("ADMIN", "MODERATOR", "RECEPTIONIST")
            .requestMatchers("/bookings/check-out").hasAnyRole("ADMIN", "MODERATOR", "RECEPTIONIST")
            .requestMatchers("/bookings/report").hasAnyRole("ADMIN", "MODERATOR", "RECEPTIONIST")
            .requestMatchers("/user/login", "/register/**","/error").permitAll()
            .requestMatchers("/").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/moderator/**").hasRole("MODERATOR")
            .requestMatchers("/receptionist/**").hasRole("RECEPTIONIST")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/user/login")
            .defaultSuccessUrl("/dashboard", true) 
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/user/login?logout")
            .permitAll();
        return http.build();
    }

}
