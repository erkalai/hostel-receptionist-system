package com.hostelbooking.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hostelbooking.entity.Role;
import com.hostelbooking.enums.RoleType;
import com.hostelbooking.repository.RoleRepository;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType) == null) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
                System.out.println("Added role: " + roleType);
            }
        }
    }
}
