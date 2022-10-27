package com.backendsem4.backend.service;

import com.backendsem4.backend.entities.Order;
import com.backendsem4.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    OrderRepository repo;

    public List<Order> listAll() {
        return (List<Order>) repo.findAll();
    }

}

