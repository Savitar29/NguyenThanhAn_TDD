package com.example.springtest.repository;

import com.example.springtest.model.Cart;

public interface ICartRepository {
    Cart findByUserEmail(String  userEmail);
    Cart save(Cart cart);
}
