package com.hostelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hostelbooking.repository")
public class HostelBooking {

    public static void main(String[] args) {
        SpringApplication.run(HostelBooking.class, args);
        
        System.out.print("Aplication Started V2 Kalai ***************************************************** 12/01/2025");
        System.out.print("Almost get booking id Ok ***************************************************** 16/01/2025");
        System.out.print("Room Change Completed ***************************************************** 17/01/2025");
        System.out.print("Room Change Completed ***************************************************** New File");
        System.out.print("Room Change Completed ***************************************************** View All users");
        System.out.print(" ################## Final File");
        System.out.print(" ################## Add Gender");
    }
}