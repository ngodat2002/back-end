package com.backendsem4.backend.service;

import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailService {
    @Autowired
    ProductRepository repo;
    public List<Product> listAll() {
        return (List<Product>) repo.findAll();
    }

}
