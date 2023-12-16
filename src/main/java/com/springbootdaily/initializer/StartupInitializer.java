package com.springbootdaily.initializer;

import com.springbootdaily.entities.Role;
import com.springbootdaily.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public StartupInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Role> roles = this.roleRepository.findAll();

        if(roles == null || roles.isEmpty()) {
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");

            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");

            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
        }

    }
}
