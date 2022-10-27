package com.backendsem4.backend.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSeeder implements CommandLineRunner {
    @Autowired
    UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        userSeeder.generate();
    }
}