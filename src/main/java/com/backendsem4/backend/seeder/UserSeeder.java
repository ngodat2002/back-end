package com.backendsem4.backend.seeder;

import com.backendsem4.backend.entities.Role;
import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.RoleRepository;
import com.backendsem4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    public static List<Role> roleList = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();
    public void generate(){
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleList.add(roleAdmin);
        roleList.add(roleUser);
        roleRepository.saveAll(roleList);

    }
}
