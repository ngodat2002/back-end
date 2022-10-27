package com.backendsem4.backend.service;

import com.backendsem4.backend.entities.User;
import com.backendsem4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository repo;
    public List<User> listAll() {
        return (List<User>) repo.findAll();
    }
}
