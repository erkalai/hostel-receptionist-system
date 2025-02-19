package com.hostelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hostelbooking.repository")
public class HostelBooking {

    public static void main(String[] args) {
        SpringApplication.run(HostelBooking.class, args);
        
        System.out.print("############### Application Started ###############");
    }
}