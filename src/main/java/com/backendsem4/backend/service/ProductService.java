package com.backendsem4.backend.service;

import com.backendsem4.backend.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> listBestSale();
    List<Product> listNew();
    List<Product> listNoiBat();
}
