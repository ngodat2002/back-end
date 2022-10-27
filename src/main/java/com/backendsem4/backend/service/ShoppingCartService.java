package com.backendsem4.backend.service;

import com.backendsem4.backend.entities.CartItem;
import com.backendsem4.backend.entities.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ShoppingCartService {

    int getCount();

    double getAmount();

    void clear();

    Collection<CartItem> getCartItems();

    void remove(CartItem item);

    void add(CartItem item);

    void remove(Product product);

}