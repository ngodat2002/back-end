package com.backendsem4.backend.service.impl;

import com.backendsem4.backend.entities.Product;
import com.backendsem4.backend.repository.ProductRepository;
import com.backendsem4.backend.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> listBestSale() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> listNew() {
        return productRepository.listProductNew20();
    }

    @Override
    public List<Product> listNoiBat() {
        return productRepository.productLimit();
    }

}

